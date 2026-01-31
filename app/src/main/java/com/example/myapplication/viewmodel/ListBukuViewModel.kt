import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.repositori.RepositoriBuku
import com.example.myapplication.room.Buku
import com.example.myapplication.view.route.DestinasiListBuku
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ListBukuViewModel(
    savedStateHandle: SavedStateHandle,
    private val repositoriBuku: RepositoriBuku
) : ViewModel() {
    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }

    val kategoriId: Int = checkNotNull(savedStateHandle[DestinasiListBuku.kategoriIdArg])

    val listBukuUiState: StateFlow<ListBukuUiState> =
        repositoriBuku.getBukuByKategoriStream(kategoriId)
            .filterNotNull()
            .map { ListBukuUiState(listBuku = it.toList()) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = ListBukuUiState()
            )

    data class ListBukuUiState(
        val listBuku: List<Buku> = listOf()
    )
}