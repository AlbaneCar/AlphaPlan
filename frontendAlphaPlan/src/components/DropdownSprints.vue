<script setup>
import Dropdown from 'v-dropdown'
import { defineProps,defineEmits, ref  } from 'vue'
import { fetchSprint } from '../services/SprintService'

const dropdown = ref(null);

// Création de la variable pour les sprints
const data = ref(null);
const selectedSprint = ref(null);

// Création de la variable pour les propriétés
const props = defineProps({
    modelValue: {
    type: Array,
    required: true
  }
});

// Création de la variable pour l'exportation du nombre de sprints
const emits = defineEmits(['update:modelValue']);

// Fonction pour mettre à jour la valeur du nombre de sprints
const updateValue = (newValue) => {
    emits('update:modelValue', newValue);
};

// Fonction pour charger les données
const loadData = async () => {
    data.value = await fetchSprint();
    if(data.value.length == 0) return;
    // On met à jour la valeur du modèle
    updateValue([data.value[0]]);
    selectedSprint.value = data.value[0];
};

// On charge les données
loadData();

// Fonction pour sélectionner une option
async function changeSprint(sprint) {
    if (selectedSprint.value.id == sprint.id) return;
    // On met à jour la valeur du modèle
    updateValue([sprint]);
    selectedSprint.value = sprint;
    // On ferme le dropdown
    dropdown.value.close();
}

</script>


<template>
    <div v-if="data && data.length != 0" class="w-[18rem] p-2 bg-gray-100 font-CASlalomBold text-xs uppercase rounded-md flex flex-row items-center justify-between">

        <span>Sprint sélectionné : </span>

        <Dropdown ref="dropdown">
            <template #trigger>
                <button id="sprint-chooser" type="button" class="w-24 py-1 px-2 bg-custom-lightblueAP hover:bg-custom-darkblueAP text-white rounded-md duration-300">
                    {{ (selectedSprint == null) ? "Aucun" : selectedSprint.name }}
                </button>
            </template>

            <div v-if="data && data.length != 0" class="flex flex-col font-CASlalomBold text-xs">
                <button v-for="sprint in data" :key="sprint.id" :active="sprint.active" @click="changeSprint(sprint)" class="w-24 hover:bg-gray-50 py-2 px-2">
                {{ sprint.name }}
                </button>
            </div>

            <div v-else class="w-[250px] py-2 px-3">
                Aucun sprint disponible
            </div>
        </Dropdown>
    </div>
</template>
