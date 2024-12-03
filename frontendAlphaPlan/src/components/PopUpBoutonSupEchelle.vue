<template>
  <div>
    <!-- Bouton de suppression -->
    <ButtonError @click="openConfirmationDialog(id)">Supprimer</ButtonError>

    <div v-if="showConfirmationDialog" class="confirmation-overlay">
      <div class="confirmation-dialog">
        <div class="confirmation-dialog-content">
          <p class="confirmation-title font-CASlalomBold">Confirmation</p>
          <p>Êtes-vous sûr de vouloir supprimer cette échelle de notes ?</p>
          <div class="button-container">
            <button @click="cancelDelete" class="font-CASlalomBold text-custom-red border-2 border-custom-red px-4 py-2 rounded-md hover:bg-red-700 hover:text-white transition-colors hover:border-red-700">Annuler</button>
            <ButtonError @click="methodSupprimer">Confirmer</ButtonError>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { defineProps } from 'vue';
import { confirmDelete } from '../services/EchelleNoteService.js';
import ButtonError from './Buttons/ButtonError.vue';

const showConfirmationDialog = ref(false);
const echellesNote = ref([]);

const props = defineProps({
    id: Number
  });
const id = ref(props.id);

async function methodSupprimer() {
  await confirmDelete(props.id);
  showConfirmationDialog.value = false;
  location.reload();
}

const openConfirmationDialog = () => {
  showConfirmationDialog.value = true;
};

const cancelDelete = () => {
  showConfirmationDialog.value = false;
};

</script>

<style scoped>
.delete-button {
  background-color: transparent;
  color: red;
  border: 2px solid red;
  border-radius: 8px;
  padding: 3px 8px; 
  font-size: 12px; 
}

.confirmation-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5); /* Fond semi-transparent gris */
  z-index: 999; /* Assure que la couche semi-transparente est au-dessus de tout le contenu */
  display: flex;
  justify-content: center;
  align-items: center;
}

.confirmation-dialog {
  background-color: white;
  border-radius: 8px;
  padding: 2px; 
  text-align: center;
  font-size: 12px; 
}

.confirmation-dialog-content {
  padding: 8px; 
  text-align: center;
  font-size: 14px; 
}

.confirmation-title {
  font-size: 16px;
  font-weight: bold;
}

.confirm-button {
  background-color: red;
  color: white;
  border: 1px solid red;
  border-radius: 10px;
  padding: 3px 8px; 
  margin-right: 5px;
}

.cancel-button {
  background-color: white;
  color: red;
  border: 1px solid red;
  border-radius: 10px;
  padding: 3px 8px; 
}

.button-container {
  margin-top: 10px;
  display: flex;
  justify-content: flex-end;
}

.button-container button {
  margin-left: 5px;
}
</style>
