package i.learn.apioptimization.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("B")
class Book(
    name: String,
    price: Int,
    stockQuantity: Int,
    val author: String? = null,
    val isbn: String? = null
): Item(name = name, price = price, stockQuantity = stockQuantity)