import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mertg.shoppingapp.model.Product
import com.mertg.shoppingapp.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {
    private val _productList = MutableStateFlow<List<Product>>(emptyList())
    val productList: StateFlow<List<Product>> = _productList

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
    private val storage: FirebaseStorage = FirebaseStorage.getInstance()

    init {
        getProducts()
    }

    fun getProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            db.collection("products").get()
                .addOnSuccessListener { documents ->
                    val products = documents.mapNotNull { it.toObject(Product::class.java).copy(id = it.id) }
                    _productList.value = products
                    _isLoading.value = false
                }
                .addOnFailureListener {
                    _isLoading.value = false
                }
        }
    }

    fun getProductById(productId: String, onSuccess: (Product) -> Unit, onFailure: (Exception) -> Unit) {
        db.collection("products").document(productId).get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)?.copy(id = document.id)
                product?.let { onSuccess(it) } ?: onFailure(Exception("Product not found"))
            }
            .addOnFailureListener { onFailure(it) }
    }

    fun addToCart(productId: String, productName: String, context: Context) {
        val userId = auth.currentUser?.uid ?: return
        val cartRef = db.collection("carts").document(userId)

        cartRef.get().addOnSuccessListener { document ->
            val currentCount = document.getLong(productId) ?: 0
            val newCount = currentCount + 1
            cartRef.update(productId, newCount).addOnSuccessListener {
                Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                cartRef.set(mapOf(productId to newCount)).addOnSuccessListener {
                    Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
                }
            }
        }.addOnFailureListener {
            cartRef.set(mapOf(productId to 1)).addOnSuccessListener {
                Toast.makeText(context, "Product added to cart", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to add product to cart", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun checkIfFavorite(productId: String, onResult: (Boolean) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        db.collection("favorites").document(userId)
            .collection("items").document(productId)
            .get()
            .addOnSuccessListener { document ->
                onResult(document.exists())
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    fun uploadProduct(
        name: String,
        description: String,
        category: String,
        price: Double,
        imageUri: Uri,
        context: Context,
        navController: NavController
    ) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("$userId/product_images/${System.currentTimeMillis()}.jpg")

        val uploadTask = storageRef.putFile(imageUri)
        uploadTask.addOnFailureListener {
            Toast.makeText(context, "Error uploading image: ${it.message}", Toast.LENGTH_SHORT).show()
        }.addOnSuccessListener { taskSnapshot ->
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val productRef = db.collection("products").document()
                val product = hashMapOf(
                    "id" to productRef.id,
                    "name" to name,
                    "description" to description,
                    "imageUrl" to uri.toString()
                )

                productRef.set(product)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Product uploaded successfully", Toast.LENGTH_SHORT).show()
                        navController.navigate(Screen.HomePage.route) {
                            popUpTo(Screen.UploadItem.route) { inclusive = true }
                        }
                    }
                    .addOnFailureListener { e ->
                        Toast.makeText(context, "Error uploading product: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }
    }

    fun toggleFavorite(product: Product, isFavorite: Boolean) {
        val userId = auth.currentUser?.uid ?: return
        val docRef = db.collection("favorites").document(userId).collection("items").document(product.id)

        if (isFavorite) {
            docRef.set(product)
        } else {
            docRef.delete()
        }
    }
}
