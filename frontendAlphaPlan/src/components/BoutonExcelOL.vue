<template>
  <div class="flex flex-col gap-6">
    <input type="file" ref="fileInput" @change="handleFileChange" class="rounded-md text-center flex flex-col p-2">
    <Button @click="uploadFile" class="upload-button">Envoyer la liste des étudiants</Button>
  </div>
</template>

<style scoped>
.upload-button {
  background-color: #32ff4a; 
  color: white; 
  padding: 10px 20px;
  font-family: CASlalomRegular;
  border: none; 
  border-radius: 5px; 
  cursor: pointer; 
  transition: background-color 0.3s; 
}
.upload-button:hover {
  background-color:#1B1336;
}
</style>

<script>
import {customFetch} from "../utils/FetchUtil";

export default {
  data() {
    return {
      selectedFile: null
    };
  },
  methods: {
    handleFileChange(event) {
      this.selectedFile = event.target.files[0];
    },
    uploadFile() {
      if (this.selectedFile) {
        let formData = new FormData();
        formData.append('file', this.selectedFile);

        customFetch('users/processExcelUtilisateur', formData, {
          method: 'POST',
          body: formData
        })
        .then(response => {
          alert('Fichier Excel envoyé avec succès !');
        })
        .catch(error => {
          alert('Erreur lors de l\'envoi du fichier. Veuillez réessayer.');
        });
      } else {
        alert('Veuillez sélectionner un fichier à envoyer.');
      }
    }
  }
}
</script>
