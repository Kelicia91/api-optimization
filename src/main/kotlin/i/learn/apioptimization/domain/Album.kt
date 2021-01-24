package i.learn.apioptimization.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("A")
class Album(
    name: String,
    price: Int,
    stockQuantity: Int,
    val artist: String? = null,
    val etc: String? = null
): Item(name = name, price = price, stockQuantity = stockQuantity)
