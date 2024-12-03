<template>
  <div id="app" class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG gap-4">

    <!-- Titre et description de la page -->
    <div class="flex flex-col gap-4">
      <Title>Gestion des échelles de notes</Title>
      <p class="text-custom-darkblueAP font-roboto text-sm">
        Visualisez ou créez des échelles de notes pour votre projet.
        Il ne peut y avoir qu'une seule échelle de notes par type.<br>
        Les échelles de notes doivent impérativement débuter à 0 et se terminer à 20.
      </p>
    </div>

    <!-- Boutons d'actions -->
    <div class="flex flex-row gap-8 h-full">
      <div class="flex-1">
        <AccordionEchelle />
      </div>
      <div class="flex-1">
        <FormEchelles :visibilite="visibleImport" @noteCreated="handleNoteCreated" />
      </div>
    </div>
  </div>
</template>

<script setup>
import AccordionEchelle from '../components/AccordionEchelle.vue';
import Title from '../components/Title.vue';
import FormEchelles from '../components/FormEchelles.vue';
import EchelleTrait from '../components/Echelle.vue';
import { ref, onMounted, computed } from 'vue';
import { fetchEchellesNote } from '../services/EchelleNoteService.js';

const visibleImport = ref(false);
const echellesNote = ref([]);
const selectedTypeEchelle = ref('');

function handleNoteCreated(newNote) {
  echellesNote.value.push(newNote);
}

onMounted(async () => {
  echellesNote.value = await fetchEchellesNote();
});

const uniqueTypes = computed(() => {
  const types = new Set();
  echellesNote.value.forEach(echelle => types.add(echelle.typeEchelle));
  return Array.from(types);
});

const filteredEchelles = computed(() => {
  if (!selectedTypeEchelle.value) {
    return echellesNote.value;
  } else {
    return echellesNote.value.filter(echelle => echelle.typeEchelle === selectedTypeEchelle.value);
  }
});
</script>

<style scoped></style>
