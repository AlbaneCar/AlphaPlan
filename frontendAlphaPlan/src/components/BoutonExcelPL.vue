<template>
  <div class="flex flex-col gap-6 justify-start items-center">
    <label class="flex flex-col justify-start items-center">
      <input type="file" ref="fileInput" class="hidden" accept=".xlsx" @change="handleFileChange">
        <Upload color="grey" width=80 />
      <div v-if="selectedFile" class="text-xs font-CASlalomRegular text-gray-400">
        {{ selectedFile.name }}
      </div>
      <div v-else class="text-xs font-CASlalomRegular text-gray-400">
        Cliquer pour ajouter un fichier au format .xlsx
      </div>
    </label>
    

    <Button v-if="!loading" @click="uploadFile2()" class="upload-button h-12 min-w-72 font-CASlalomBold">Envoyer le fichier Excel</Button>
    <button v-else class="upload-button h-12 min-w-72">
      <Loader />
    </button>
    
  </div>
</template>


<script setup>
import { ref } from 'vue';
import {useToast} from 'vue-toastification';
import {uploadFileS} from '../services/EtudiantNotesService';
import Loader from '../components/Loader/Loader.vue';
import Upload from '../assets/icons/Upload.vue';
import Button from '../components/Buttons/Button.vue';  

const toast = useToast();
const selectedFile = ref(null);
const loading = ref(false);

const handleFileChange = (event) => {
  selectedFile.value = event.target.files[0];
};

function uploadFile2(){
  uploadFileS(selectedFile, loading)
}


</script>
