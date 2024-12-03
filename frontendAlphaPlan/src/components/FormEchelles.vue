<template>
    <div class="p-8 flex flex-col bg-white rounded-md shadow-sm border border-gray-200">
      <h2 class="text-lg font-bold mb-2">Créer une échelle de notes</h2>
      <form @submit.prevent="onSubmitForm" class="flex flex-col gap-1">
        <label for="type">Type:</label>
        <select id="type" v-model="form.typeEchelle" required class="border border-gray-300 rounded-md px-2 py-1">
          <option value="" disabled>Sélectionnez un type</option>
          <option v-for="type in filteredTypes" :key="type" :value="type">{{ type }}</option>
        </select>

        <label for="description">Description:</label>
        <textarea id="description" v-model="form.description" required class="border border-gray-300 rounded-md px-2 py-1"
                  :style="{ minHeight: '30px', maxHeight: '60px' }"></textarea>

        <div v-for="(echelon, index) in form.echelons" :key="index" class="flex items-center gap-2 mt-2">
          <label class="flex items-center gap-2">
            De 
            <input type="number" v-model.number="echelon.note1" class="border border-gray-300 rounded-md px-2 py-1 min-w-10" min=0 max=20 required>
          </label>
          <label class="flex items-center gap-2">
            à
            <input type="number" v-model.number="echelon.note2" class="border border-gray-300 rounded-md px-2 py-1 min-w-10" min=0 max=20 required>
          </label>
          <label class="flex items-center gap-2">
             indications: 
            <input type="text" v-model="echelon.commentaire" class="border border-gray-300 rounded-md px-2 py-1 flex-1" required>
          </label>
          <ButtonError type="button" @click="removeEchelon(index)" class="remove-button ml-2">Supprimer</ButtonError>
        </div>

        <Button icon='plus' type="button" @click="addEchelon" classes=" mt-6 px-4 py-2 p-2">Ajouter un échelon</Button>
        <Button icon='plus' type="submit" classes="px-4 py-2 mt-4">Créer l'échelle</Button>
      </form>
    </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue';
import { fetchTypesEchelle, fetchUsedTypesEchelle, submitForm } from '../services/EchelleNoteService';
import Button from './Buttons/Button.vue';
import ButtonError from './Buttons/ButtonError.vue';
import { useToast } from 'vue-toastification';
import { CircleCheckBig } from 'lucide-vue-next';

const toast = useToast();
const form = ref({
  typeEchelle: '',
  description: '',
  echelons: [
    { note1: '', note2: '', commentaire: '' }
  ]
});

const types = ref([]);
const usedTypes = ref([]);
const filteredTypes = computed(() => types.value.filter(type => !usedTypes.value.includes(type)));

onMounted(() => {
  fetchTypesEchelle()
    .then(data => types.value = data)
    .catch(error => console.error(error));

  fetchUsedTypesEchelle()
    .then(data => usedTypes.value = data)
    .catch(error => console.error(error));
});

function addEchelon() {
  form.value.echelons.push({ note1: '', note2: '', commentaire: '' });
}

function removeEchelon(index) {
  form.value.echelons.splice(index, 1);
}

function validateEchelons() {
  const uniquePairs = new Set();
  let hasNote1Zero = false;
  let hasNote2Twenty = false;

  for (const [index, echelon] of form.value.echelons.entries()) {
    const { note1, note2 } = echelon;

    if (note1 < 0 || note1 > 20 || note2 < 0 || note2 > 20) {
      toast.error("Les notes doivent être comprises entre 0 et 20.", { position: "top-center" });
      return false;
    }

    if (note1 === note2) {
      toast.error("Les notes de chaque échelon doivent être différentes.", { position: "top-center" });
      return false;
    }

    if (note1 > note2) {
      toast.error("La note de début doit être inférieure à la note de fin.", { position: "top-center" });
      return false;
    }

    for (const [otherIndex, otherEchelon] of form.value.echelons.entries()) {
      if (index !== otherIndex) {
        if (!(note2 < otherEchelon.note1 || note1 > otherEchelon.note2)) {
          toast.error("Les échelons ne doivent pas se chevaucher.", { position: "top-center" });
          return false;
        }
      }
    }

    const pair = `${note1}-${note2}`;
    if (uniquePairs.has(pair)) {
      toast.error("Les paires de notes doivent être uniques.", { position: "top-center" });
      return false;
    }
    uniquePairs.add(pair);

    if (note1 === 0) {
      hasNote1Zero = true;
    }
    if (note2 === 20) {
      hasNote2Twenty = true;
    }
  }

  if (!hasNote1Zero) {
    toast.error("Il doit y avoir au moins une note égale à 0.", { position: "top-center" });
    return false;
  }

  if (!hasNote2Twenty) {
    toast.error("Il doit y avoir au moins une note égale à 20.", { position: "top-center" });
    return false;
  }

  return true;
}


async function onSubmitForm() {
  if (!validateEchelons()) {
    return;
  }

  try {
    await submitForm(form.value);
    form.value = {
      typeEchelle: '',
      description: '',
      echelons: [
        { note1: '', note2: '', commentaire: '' }
      ]
    };
    setTimeout(() => {
      location.reload();
    }, 3000);
  } catch (error) {
    toast.error("Une erreur est survenue lors de la création de l'échelle de notes.", { position: "top-center" });
  }
}
</script>

<style scoped>
input[type="number"], textarea, select {
  border: 1px solid #ccc;
  border-radius: 4px;
  padding: 6px;
  margin-bottom: 6px;
  box-sizing: border-box;
}

input[type="number"] {
  width: calc(100% - 12px);
}

.flex input[type="number"] {
  margin-bottom: 0;
}

label.flex {
  flex-direction: row;
  align-items: center;
}
</style>
