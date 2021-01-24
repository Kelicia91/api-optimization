package i.learn.apioptimization.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("B")
data class Book(
    val author: String? = null,
    val isbn: String? = null
): Item()
