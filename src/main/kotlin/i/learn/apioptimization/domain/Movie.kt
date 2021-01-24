package i.learn.apioptimization.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("M")
class Movie(
    name: String,
    price: Int,
    stockQuantity: Int,
    val director: String? = null,
    val actor: String? = null
): Item(name = name, price = price, stockQuantity = stockQuantity)
