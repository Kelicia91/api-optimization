package i.learn.apioptimization.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("M")
data class Movie(
    val director: String? = null,
    val actor: String? = null
): Item()
