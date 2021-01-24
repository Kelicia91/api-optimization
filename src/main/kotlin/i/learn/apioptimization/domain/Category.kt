package i.learn.apioptimization.domain

import javax.persistence.*

@Entity
data class Category(
    @Id
    @GeneratedValue
    @Column(name = "category_id")
    var id: Long? = null,

    val name: String? = null,

    // @note: 설명을 위해 추가됨.
    // - ManyToMany는 'category_item' table에 column을 추가할 수 없고 detail-query 실행이 어려우니 사용 x
    // - 대신 'order_item' table처럼 주문과 상품 간의 관계를 n:n으로 두지 않고 1:n<->n:1으로 대체
    @ManyToMany
    @JoinTable(
        name = "category_item",
        joinColumns = [JoinColumn(name = "category_id")],
        inverseJoinColumns = [JoinColumn(name = "item_id")]
    )
    val items: MutableList<Item> = mutableListOf(),

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    var parent: Category? = null,

    @OneToMany(mappedBy = "parent")
    val child: MutableList<Category> = mutableListOf()
) {
    //==연관관계 메서드==//
    fun addChildCategory(child: Category) {
        this.child.add(child)
        child.parent = this
    }
}
