<template>
  <div>
    <!-- Bouton de suppression -->
    <ButtonError @click="openConfirmationDialog(id)" class="delete-button font-CASlalomBold">Supprimer la conversation</ButtonError>

    <!-- Boîte de dialogue de confirmation -->
    <div v-if="showConfirmationDialog" class="confirmation-dialog">
      <div class="confirmation-dialog-content">
        <p class="confirmation-title font-CASlalomBold">Confirmation</p>
        <p>Êtes-vous sûr de vouloir supprimer cette conversation ?</p>
        <div class="button-container">
          <button @click="cancelDelete" class="cancel-button font-CASlalomBold">Annuler</button>
          <button @click="suppConv" class="confirm-button font-CASlalomBold">Confirmer</button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { defineProps } from 'vue';
import { suppConversation } from '../services/ConversationsService.js';
import ButtonError from '../components/Buttons/ButtonError.vue';
import { useToast } from 'vue-toastification';

const toast = useToast();

const showConfirmationDialog = ref(false);

const props = defineProps({
    id: Number
  });
const id = ref(props.id);

const suppConv = () => {
    suppConversation(props.id)
      .then(() => {
        toast.success("La conversation a été supprimée !", {
          position: "top-center",
        });
        setTimeout(() => {
          location.reload();
        }, 2000);
      })
      .catch(error => {
        toast.error("Une erreur s'est produite : " + error, {
          position: "top-center",
        });
      });
};

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

.confirmation-dialog {
  position: fixed;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(228, 228, 228, 0.666); /* Fond semi-transparent */
  border-radius: 8px;
  padding: 2px;
  text-align: center;
  font-size: 12px;
  z-index: 9999;
}

.confirmation-dialog-content {
  background-color: white;
  border-radius: 10px;
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
