package org.freedu.nsda_job1_xml


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.freedu.nsda_job1_xml.databinding.ActivityProductBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductBinding  // ViewBinding for activity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvProducts.layoutManager = LinearLayoutManager(this)

        binding.btnRetry.setOnClickListener {
            binding.btnRetry.visibility = View.GONE
            fetchProducts()
        }

        fetchProducts()
    }

    @SuppressLint("MissingPermission")
    private fun fetchProducts() {
        if (!NetworkUtils.isInternetAvailable(this)) {
            Toast.makeText(this, "No internet connection", Toast.LENGTH_SHORT).show()
            binding.btnRetry.visibility = View.VISIBLE
            return
        }

        ApiClient.apiService.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(
                call: Call<List<Product>>,
                response: Response<List<Product>>
            ) {
                if (response.isSuccessful && response.body() != null) {
                    val products = response.body()!!
                    binding.rvProducts.adapter = ProductAdapter(products) { product ->
                        // Handle item click
                        Toast.makeText(
                            this@ProductActivity,
                            "Clicked: ${product.title}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                } else {
                    Toast.makeText(this@ProductActivity, "Failed to fetch data", Toast.LENGTH_SHORT)
                        .show()
                    binding.btnRetry.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Toast.makeText(this@ProductActivity, "Error: ${t.message}", Toast.LENGTH_SHORT)
                    .show()
                binding.btnRetry.visibility = View.VISIBLE
            }
        })
    }
}

