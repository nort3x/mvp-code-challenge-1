package com.github.nort3x.backendchallenge1

import com.github.nort3x.backendchallenge1.controller.ProductController
import com.github.nort3x.backendchallenge1.controller.ShoppingController
import com.github.nort3x.backendchallenge1.controller.UserController
import com.github.nort3x.backendchallenge1.dto.Coin
import com.github.nort3x.backendchallenge1.exceptions.Insufficient
import com.github.nort3x.backendchallenge1.exceptions.UnAuthorized
import com.github.nort3x.backendchallenge1.model.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.assertDoesNotThrow
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpStatus
import org.springframework.security.test.context.support.WithAnonymousUser
import org.springframework.test.annotation.DirtiesContext
import org.springframework.test.context.ActiveProfiles
import java.util.stream.Stream
import javax.validation.ConstraintViolationException
import kotlin.streams.asStream
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ApiTest {

    // region setup
    @Autowired
    lateinit var userSwitcher: UserSwitcher

    @Autowired
    lateinit var userController: UserController

    val buyer = UserDef("user1", VendingMachineUserRole.BUYER)
    val seller = UserDef("user2", VendingMachineUserRole.SELLER)


    // endregion

    // region UserCRUDTest

    private fun generateUsers(): Stream<Arguments> =

        sequence {
            for (i in 0..3)
                yield(
                    VendingMachineUser(
                        randomString(),
                        randomString(),
                        VendingMachineUserRole.values().random()
                    )
                )
        }.map {
            Arguments.of(it)
        }.asStream()

    private fun VendingMachineUser.asRegisterDto(): VendingMachineUserRegisterDto =
        VendingMachineUserRegisterDto(username, password, role)

    @ParameterizedTest
    @MethodSource("generateUsers")
    @WithAnonymousUser
    fun `POST - user registration`(user: VendingMachineUser) =
        assertDoesNotThrow {
            with(userController.registerNewUser(user.asRegisterDto())) {
                assertEquals(statusCode, HttpStatus.CREATED)

                assertNotNull(this.body)
                assertNotNull(this.body?.role)
                assertNotNull(this.body?.password)
                assertNotNull(this.body?.username)

                println(this)
            }
        }

    @Test
    fun `PUT - update user`() {
        userSwitcher.asUserWithTransaction(buyer) {
            with(userController.updateUser(UserUpdateDto("new-password", VendingMachineUserRole.SELLER), it.username)) {

                assertEquals(statusCode, HttpStatus.OK)

                assertNotNull(this.body?.role)
                assertNotNull(this.body?.password)
                assertNotNull(this.body?.username)

                assertEquals(this.body?.role, VendingMachineUserRole.SELLER)

                println(this)

            }
        }
    }

    @Test
    fun `GET - get user`() {
        userSwitcher.asUserWithTransaction(buyer) {} // to just make user
        userSwitcher.asUserWithTransaction(buyer) {
            val user = userController.getUserInfo(buyer.username).body
            assertEquals(user, it)
        }

    }

    @Test
    fun `DELETE - delete user`() {
        userSwitcher.asUserWithTransaction(buyer) {} // to just make user
        userSwitcher.asUserWithTransaction(buyer) {
            val user = userController.getUserInfo(buyer.username).body
            assertEquals(user, it)
        }
        userSwitcher.asUserWithTransaction(buyer) {
            val user = userController.deleteUser(it.username)
            assertFalse(userController.userService.repo.existsById(it.username))
        }


    }
    // endregion

    // region ProductTest


    @Autowired
    lateinit var productController: ProductController

    @Test
    fun `ALL - CRUD operations on Product with constraint tests`() {
        // initially we have no products
        userSwitcher.asUser(buyer) {
            assertTrue {
                productController.getAllProducts().body?.isEmpty() ?: false
            }
        }

        // buyer can't register product
        userSwitcher.asUser(buyer) {
            assertThrows<UnAuthorized> {
                productController.registerNewProduct(ProductRegisterDto(randomString(), 100, 100))
            }
        }

        userSwitcher.asUser(seller) {

            val product = productController.registerNewProduct(ProductRegisterDto(randomString(), 100, 100))
                .body!!
            val updatedProduct = productController.updateProduct(product.productId!!, ProductUpdateDto(105, 0))
                .body!!

            assertEquals(updatedProduct.cost, 105)
            assertEquals(updatedProduct.amountAvailable, 0)

            // invalid update attempt
            assertThrows<ConstraintViolationException> {
                productController.updateProduct(product.productId!!, ProductUpdateDto(99, -1))
            }

            val takenProduct = productController.getProduct(product.productId!!).body!!

            assertEquals(updatedProduct, takenProduct)

            productController.deleteProduct(product.productId!!)

            assertFalse(productController.productService.productRepo.existsById(product.productId!!))
        }
    }

    // endregion

    // region shoppingTest
    @Autowired
    lateinit var shoppingController: ShoppingController

    @Test
    fun `shopping edge case tests`() {
        var product: Product? = null
        userSwitcher.asUser(seller) {
            product = productController.registerNewProduct(ProductRegisterDto("product1", 5, 20)).body
        }


        fun buyOneProduct(product: Product) {
            shoppingController.buyProduct(product.productId!!, 1)
        }

        userSwitcher.asUser(buyer) {
            assertThrows<Insufficient> {
                buyOneProduct(product!!)
            }

            userController.depositCoin(Coin(20))
            userController.depositCoin(Coin(20))
            userController.depositCoin(Coin(50))
            // total of 90

            assertEquals(userController.getUserInfo(it.username).body?.deposit, 90)

            repeat(18) {
                buyOneProduct(product!!)
            }

            assertEquals(userController.getUserInfo(it.username).body?.deposit, 0)

            assertThrows<Insufficient> {
                buyOneProduct(product!!)
            }

            userController.depositCoin(Coin(10))
            userController.depositCoin(Coin(10))


            repeat(2) {
                buyOneProduct(product!!)
            }

            assertThrows<Insufficient> {
                buyOneProduct(product!!) // now because product amount is 0
            }

            assertEquals(userController.getUserInfo(buyer.username).body?.deposit, 10)
            assertEquals(productController.getProduct(product?.productId!!).body?.amountAvailable, 0)

            userController.resetUserDeposit()

            assertEquals(userController.getUserInfo(buyer.username).body?.deposit, 0)

        }

    }
    // endregion
}