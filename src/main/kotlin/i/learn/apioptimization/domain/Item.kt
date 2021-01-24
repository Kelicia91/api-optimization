package i.learn.apioptimization.domain

import javax.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
open class Item(
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    open var id: Long? = null,

    open val name: String? = null,

    open val price: Int = 0,

    open val stockQuantity: Int = 0,

    @ManyToMany(mappedBy = "items")
    open val categories: MutableList<Category> = mutableListOf()
)