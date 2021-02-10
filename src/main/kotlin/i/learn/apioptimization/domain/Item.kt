package i.learn.apioptimization.domain

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
class Item(
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    var id: Long? = null,

    val name: String,

    val price: Int = 0,

    val stockQuantity: Int = 0,

    @ManyToMany(mappedBy = "items")
    var categories: MutableList<Category> = mutableListOf()
)