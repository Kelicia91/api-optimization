package i.learn.apioptimization.domain

import javax.persistence.DiscriminatorValue
import javax.persistence.Entity

@Entity
@DiscriminatorValue("A")
data class Album(
    val artist: String? = null,
    val etc: String? = null
): Item()
