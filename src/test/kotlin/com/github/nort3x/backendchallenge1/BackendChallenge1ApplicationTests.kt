package com.github.nort3x.backendchallenge1

import com.github.nort3x.backendchallenge1.model.ProductSellerPK
import com.github.nort3x.backendchallenge1.model.Product
import com.github.nort3x.backendchallenge1.model.VendingMachineUser
import com.github.nort3x.backendchallenge1.repository.ProductRepo
import com.github.nort3x.backendchallenge1.repository.VendingMachineUserRepo
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import kotlin.test.assertEquals


@Component
class TestOperations(
    val vendingMachineUserRepo: VendingMachineUserRepo,
    val productRepo: ProductRepo
){


    @Transactional
    fun insertSomeStuffIntoDb(){
        val user = vendingMachineUserRepo.save(VendingMachineUser("salam","salam"))
        val product = productRepo.save(Product(user,"salam"))

        // after a while

        val product2 = productRepo.findById(ProductSellerPK(user.username,"salam")).get()

        assertEquals(product2,product)



    }


}

@SpringBootTest
class BackendChallenge1ApplicationTests {

    @Autowired
    lateinit var testOperations: TestOperations


    @Test
    fun contextLoads() {
        testOperations.insertSomeStuffIntoDb()
    }

}
