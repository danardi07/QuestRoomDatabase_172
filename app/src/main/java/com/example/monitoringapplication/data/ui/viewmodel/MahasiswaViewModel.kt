package com.example.monitoringapplication.data.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.monitoringapplication.repository.RepositoryMhs
import kotlinx.coroutines.launch


class MahasiswaViewModel(private val repositoryMhs: RepositoryMhs) : ViewModel(){
    var uiState by mutableStateOf(MhsUIState())

    fun updateState(mahasiswaEvent: MahasiswaEvent) {
        uiState = uiState.copy(
            mahasiswaEvent = mahasiswaEvent,
        )

    }

    private fun validasiFields(): Boolean {
        val event = uiState.mahasiswaEvent
        val errorState = FormErrorState(
            nim = if (event.nim.isNoEmpty()) null else "nim tidak boleh kosong",
            nama = if (event.nama.isNoEmpty()) null else "nama tidak boleh kosong",
            jenisKelamin = if (event.jenisKelamin.isNoEmpty()) null else "jenis Kelamin tidak boleh kosong",
            alamat = if (event.alamat.isNoEmpty()) null else "alamat tidak boleh kosong",
            kelas = if (event.kelas.isNoEmpty()) null else "kelas tidak boleh kosong",
            angkatan = if (event.angkatan.isNoEmpty()) null else "angkatan tidak boleh kosong"

        )
        uiState = uiState.copy(isEntryValid = errorState)
        return errorState.isValid()
    }

    fun saveData() {
        val currentEvent = uiState.mahasiswaEvent
        if (validasiFields()) {
            viewModelScope.launch {
                try {
                    repositoryMhs.insertMhs(currentEvent.toMahasiswaEntity())
                    uiState = uiState.copy(
                        snackBarMessage = "Data Berhasil disimpan",
                        mahasiswaEvent = MahasiswaEvent(),
                        isEntryValid = FormErrorState()
                    )
                }catch (e: Exception){
                    uiState = uiState.copy(
                        snackBarMessage = "Data Gagal disimpan"
                    )
                }
            }
        } else {
            uiState = uiState.copy (
                snackBarMessage = " input tidak valid. Periksa Kembali data anda"
            )
        }
    }
    fun resetSnackBarMessage() {
        uiState = uiState.copy(snackBarMessage = null)
    }

}

data class MhsUIState(
    val mahasiswaEvent: MahasiswaEvent = MahasiswaEvent(),
    val isEntryValid: FormErrorState = FormErrorState(),
    val snackBarMessage: String? = null
)


