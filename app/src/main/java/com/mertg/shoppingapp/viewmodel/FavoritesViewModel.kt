import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import com.mertg.shoppingapp.model.Product
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavoritesViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val _favoriteItems = MutableStateFlow<List<Product>>(emptyList())
    val favoriteItems = _favoriteItems.asStateFlow()
    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        getFavoriteItems()
    }

    fun getFavoriteItems() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        _isLoading.value = true
        if (userId != null) {
            db.collection("favorites").document(userId)
                .collection("items")
                .addSnapshotListener { snapshot, error ->
                    if (error != null) {
                        _isLoading.value = false
                        return@addSnapshotListener
                    }
                    val products = snapshot?.documents?.mapNotNull { it.toObject(Product::class.java) }
                    _favoriteItems.value = products ?: emptyList()
                    _isLoading.value = false
                }
        } else {
            _favoriteItems.value = emptyList()
            _isLoading.value = false
        }
    }

}
