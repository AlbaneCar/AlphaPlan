<script setup>
import { ref, watch, defineProps } from 'vue';
import { useToast } from "vue-toastification";
import { updateSprint } from '../../services/SprintService.js';
import Dialog from 'primevue/dialog';

const toast = useToast();
const visible = ref(false);
const sprints = ref([]);
const index = ref(0);

const props = defineProps({
    visibilite: Boolean,
    sprints: Array,
    index: Number
});
sprints.value = props.sprints;
index.value = props.index;

const currentSprint = ref(sprints.value[index.value]);

const oldStartDate = ref(sprints.value[index.value].startDate);
const oldEndDate = ref(sprints.value[index.value].endDate);
const oldSprintEndType = ref(sprints.value[index.value].sprintEndType);

const emits = defineEmits(['update:visibilite', 'close']);

watch(() => props.visibilite, (newValue) => {
    visible.value = newValue;
});

// Fonction permettant de calculer la date minimale de début
const getMinStartDate = () => {
    // On va regarder si le sprint actuel n'est pas le premier
    if (index.value !== 0) {
        // Si c'est le cas, on va récupérer la date de fin du sprint précédent
        const endDate = sprints.value[index.value - 1].endDate;
        return endDate;
    }
};

// Fonction permettant de calculer la date maximale de fin
const getMaxEndDate = () => {
    // On va regarder si le sprint actuel n'est pas le dernier
    if (index.value !== sprints.value.length - 1) {
        // Si c'est le cas, on va récupérer la date de début du sprint suivant
        const startDate = sprints.value[index.value + 1].startDate;
        return startDate;
    }
};

// Fonction permettant de formatter la date
const formatDate = (date) => {
    // On sépare les éléments de la date
    const [year, month, day] = date.split('-');
    // On retourne la date formatée
    return `${day}/${month}/${year}`;
};

// Fonction qui permettant de formatter le type de fin de sprint
const formatSprintEndType = (sprintEndType) => {
    // On retourne le type de fin de sprint
    return sprintEndType === 'PRESENTATION' ? 'Présentation' : 'Revue de sprint';
};

// Fonction permettant de fermer le dialog
const closeDialog = () => {
    visible.value = false;
    emits('update:visibilite', false);
    emits('close', index.value);
    emits('close', sprints.value);
};

// Fonction permettant de valider les modifications
const validateModifications = () => {
    // On récupère les nouvelles valeurs
    const newStartDate = ref(currentSprint.value.startDate);
    const newEndDate = ref(currentSprint.value.endDate);
    const newSprintEndType = ref(currentSprint.value.sprintEndType);
    // On vérifie si la date de début est bien inférieure à la date de fin
    if (newStartDate.value < newEndDate.value) {
        // On regarde si le sprint actuel n'est pas le premier
        if (index.value !== 0) {
            // Si c'est le cas, on va récupérer la date de fin du sprint précédent
            const previousEndDate = sprints.value[index.value - 1].endDate;
            // On vérifie si la date de début est bien supérieure à la date de fin du sprint précédent
            if (newStartDate.value < previousEndDate) {
                // Sinon, on affiche un toast d'erreur
                toast.error('La date de début doit être supérieure à la date de fin du sprint précédent (' + formatDate(previousEndDate) + ')');
                return;
            }
        }
        // On regarde si le sprint actuel n'est pas le dernier
        if (index.value !== sprints.value.length - 1) {
            // Si c'est le cas, on va récupérer la date de début du sprint suivant
            const nextStartDate = sprints.value[index.value + 1].startDate;
            // On vérifie si la date de fin est bien inférieure à la date de début du sprint suivant
            if (newEndDate.value > nextStartDate) {
                toast.error('La date de fin doit être inférieure à la date de début du sprint suivant (' + formatDate(nextStartDate) + ')');
                return;
            }
        }
        // Si c'est le cas, on affiche un toast de succès
        toast.success('Les modifications ont bien été prises en compte');

        // On met à jour le sprint actuel dans la base de données
        updateSprint(currentSprint.value);

        // On ferme le dialog
        closeDialog();
    } else {
        // Sinon, on affiche un toast d'erreur
        toast.error('La date de début doit être inférieure à la date de fin');
        return;
    }
};

// Fonction permettant d'annuler les modifications
const cancelModifications = () => {
    // On remet les anciennes valeurs
    currentSprint.value.startDate = oldStartDate.value;
    currentSprint.value.endDate = oldEndDate.value;
    currentSprint.value.sprintEndType = oldSprintEndType.value;
    // On ferme le dialog
    closeDialog();
};

</script>

<template>
    <Dialog v-model:visible="visible" modal :closable="false" :header="'Modification du sprint ' + (index+1)" class="w-fit">
        <div class="flex flex-col justify-around items-center w-full h-fit">

            <div class="flex flex-row w-full items-center justify-between mb-5">
                <div class="flex flex-col w-full mr-5">
                    <label class="block text-custom-darkblueAP items-center">Nouvelle date de début:</label>
                    <label class="italic text-xs text-gray-500">Ancienne date: {{ formatDate(oldStartDate) }}</label>
                </div>
                <input type="date" v-model="currentSprint.startDate"
                    class="border min-w-48 focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1"
                    :min="getMinStartDate" />
            </div>

            <div class="flex flex-row w-full items-center justify-between mb-5">
                <div class="flex flex-col w-full mr-5">
                    <label class="block text-custom-darkblueAP items-center">Nouvelle date de fin:</label>
                    <label class="italic text-xs text-gray-500">Ancienne date: {{ formatDate(oldEndDate) }}</label>
                </div>
                <input type="date" v-model="currentSprint.endDate"
                    class="border min-w-48 focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1"
                    :max="getMaxEndDate" />
            </div>

            <div class="flex flex-row w-full items-center justify-between mb-5">
                <div class="flex flex-col w-full mr-5">
                    <label class="block text-custom-darkblueAP items-center">Nouveau type de fin de sprint:</label>
                    <label class="italic text-xs text-gray-500">Ancien type: {{ formatSprintEndType(oldSprintEndType) }}</label>
                </div>
                <select v-model="currentSprint.sprintEndType"
                    class="border min-w-48 focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1">
                    <option value="PRESENTATION">Présentation</option>
                    <option value="SPRINT_REVIEW">Revue de sprint</option>
                </select>
            </div>
        </div>

        <div class="flex flex-row justify-between items-center w-full mt-3">
            <button class="px-3 py-2 bg-white text-red-500 border-red-500 border-2 rounded-md font-bold hover:bg-red-500 hover:text-white duration-300 ease-in-out" @click="cancelModifications">Annuler</button>
            <button class="px-3 py-2 bg-green-500 border-green-500 border-2 text-white rounded-md font-bold hover:bg-green-600 hover:border-green-600 duration-300 ease-in-out" @click="validateModifications">Valider</button>
        </div>
        
    </Dialog>
</template>