package org.freedu.nsda_job1_xml

data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val images: List<String>
)

