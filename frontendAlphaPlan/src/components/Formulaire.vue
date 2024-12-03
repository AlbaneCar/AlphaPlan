<template>
    <div class="w-full h-full flex flex-col gap-3">

        <!-- Composant Steps -->
        <div class="border bg-white shadow-sm border-gray-200 rounded-md p-4 h-fit">
            <Steps v-model:activeStep="active" :model="items"></Steps>
        </div>

        <div v-if="active >= 0 && active < numberOfSprints" class="border bg-white shadow-sm border-gray-200 rounded-md p-4">

            <div class="flex">
                <!-- Étapes pour chaque sprint -->
                <h2 class="text-xl font-Roboto font-bold mb-4 mr-2  text-custom-darkblueAP">Sprint {{ active + 1 }} -</h2>
                <!-- Formulaire pour définir le protocole de fin de sprint -->
                <h3 class="text-lg font-Roboto mb-2  text-custom-darkblueAP">Protocole de sprint</h3>
            </div>
            <div class="flex flex-row justify-between">
                <div class="flex flex-col">
                    <label class="block font-Roboto  text-custom-darkblueAP items-center ">Date de début :</label>
                    <input type="date" v-model="sprintData[active].startDate"
                        class="border-[1px] border-black focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1 font-Roboto mb-2"
                        :min="getMinStartDate()" />
                </div>
                <div class="flex flex-col">
                    <label class=" block font-Roboto  text-custom-darkblueAP ">Date de fin :</label>
                    <input type="date" v-model="sprintData[active].endDate" :min="getMinEndDate()"
                        class=" border-[1px] border-black focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1 font-Roboto mb-2" />
                </div>
                <div class="flex flex-col">
                    <label class="block font-Roboto  text-custom-darkblueAP ">Type de fin :</label>
                    <select v-model="sprintData[active].sprintEndType"
                        class="border-[1px] border-black focus:outline-none focus:border-custom-lightblueAP transition-colors text-gray-500 rounded-md px-2 py-1 font-Roboto mb-2">
                        <option value="Sprint Review">Sprint Review</option>
                        <option value="Présentation">Présentation</option>
                    </select>
                </div>
            </div>

            <div class="flex flex-row justify-between mt-2">
                <button @click="goBack" :disabled="active === 0" class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1 font-CASlalomBold
                    hover:bg-custom-darkblueAP duration-300 hover:transition-colors disabled:bg-gray-300
                    disabled:text-gray-600">Retour</button>
                <button @click="goForward" :disabled="!isSprintFormFilled(active)"
                    class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1 font-CASlalomBold hover:bg-custom-darkblueAP duration-300  hover:transition-colors disabled:bg-gray-300 disabled:text-gray-600">Suivant</button>
            </div>

        </div>

        <div v-else-if="active === numberOfSprints"
            class="border bg-white shadow-sm border-gray-200 rounded-md p-4">
            <!-- Toutes les étapes sont terminées -->
            <h2 class="text-2xl font-CASlalomBold text-center mb-1  text-custom-darkblueAP ">Merci !</h2>
            <div class="flex flex-col md:flex-row items-center justify-between space-x-2">
                <button @click="goBack" :disabled="formSubmitted || formSubmitSuccess"
                    class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1 font-CASlalomBold hover:bg-custom-darkblueAP duration-300  hover:transition-colors disabled:bg-gray-300 disabled:text-gray-600">Retour</button>
                <button @click="submitForm" :disabled="formSubmitted || formSubmitSuccess"
                    class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1  font-CASlalomBold hover:bg-custom-darkblueAP duration-300 hover:transition-colors disabled:bg-gray-300 disabled:text-gray-600">Soumettre</button>
            </div>
        </div>

    </div>

</template>

<script setup>

import { ref, defineEmits } from 'vue';
import Steps from 'primevue/steps';
import { fetchData } from '../services/FormulaireSprint.js';
import { useToast } from "vue-toastification";

// Définition de la variable active et de la variable n
const active = ref(0);
const n = ref(1);
const props = defineProps({
    nombre: Number
});
const numberOfSprints = ref(props.nombre);
const sprintData = ref([]);
const toast = useToast();
const items = [];
const formSubmitted = ref(false);
const formSubmitSuccess = ref(false);

const emit = defineEmits(['update:formSubmitSuccess']);

const createSteps = () => {
    sprintData.value = [];
    items.value = [];
    for (let i = 0; i < numberOfSprints.value; i++) {
        items.push({ label: 'Sprint ' + (i + 1) });
        sprintData.value.push({ startDate: '', endDate: '', sprintEndType: '' });
    }
};

createSteps();

//onMounted(createSteps);

// Fonction pour passer à l'étape suivante
const goForward = () => {
    if (isSprintFormFilled(active.value)) {
        active.value++; // Incrémentez simplement active.value pour passer à l'étape suivante
    } else {
        // Afficher un message d'erreur
        alert('Veuillez remplir tous les champs du formulaire avant de passer à l\'étape suivante.');
    }
};

// Vérifier si le formulaire du sprint actuel est rempli
const isSprintFormFilled = (index) => {
    const sprint = sprintData.value[index];
    return sprint && sprint.startDate && sprint.endDate && sprint.sprintEndType;
}

// Fonction pour revenir à l'étape précédente
const goBack = () => {
    if (active.value > 0) {
        active.value--;
        n.value--;
    }
};

const getMinStartDate = () => {
    if (active.value === 0) {
        // Pour le premier sprint, la date minimale est la date actuelle
        return getCurrentDate();
    } else {
        // Pour les autres sprints, la date minimale est la date de fin du sprint précédent
        const previousSprintEndDate = sprintData.value[active.value - 1].endDate;
        return previousSprintEndDate;
    }
};

const getMinEndDate = () => {
    // La date minimale de la date de fin est la date de début
    const startDate = sprintData.value[active.value].startDate;
    return startDate;
};

const getCurrentDate = () => {
    const currentDate = new Date();
    const year = currentDate.getFullYear();
    const month = ('0' + (currentDate.getMonth() + 1)).slice(-2); // Ajoute un zéro devant les mois < 10
    const day = ('0' + currentDate.getDate()).slice(-2); // Ajoute un zéro devant les jours < 10
    return `${year}-${month}-${day}`;
};

const submitForm = async () => {
    for (const currentSprint of sprintData.value) {
        if (!currentSprint || !currentSprint.startDate || !currentSprint.endDate || !currentSprint.sprintEndType) {
            toast.error('Veuillez remplir tous les champs du formulaire avant de soumettre.', { position: "top-center" });
            return; // Arrêtez le processus de soumission
        }
    }

    // Vérification des dates pour chaque sprint
    for (let i = 0; i < sprintData.value.length; i++) {
        const sprint = sprintData.value[i];

        // Vérifier si la date de début est antérieure à la date actuelle
        if (new Date(sprint.startDate) < new Date()) {
            toast.error('La date de début du sprint ne peut pas être antérieure à la date actuelle.', { position: "top-center" });
            return; // Arrêter l'envoi des données
        }

        // Vérifier si la date de fin est antérieure à la date de début
        if (new Date(sprint.endDate) < new Date(sprint.startDate)) {
            toast.error('La date de fin du sprint ne peut pas être antérieure à la date de début.', { position: "top-center" });
            return; // Arrêter l'envoi des données
        }

        // Vérifier si les dates du sprint suivant sont antérieures à celles du sprint actuel
        if (i > 0) {
            const previousSprint = sprintData.value[i - 1];
            if (new Date(sprint.startDate) < new Date(previousSprint.endDate)) {
                toast.error('Les dates du Sprint ' + (i + 1) + ' ne peuvent pas être antérieures à celles du Sprint ' + i + '.', { position: "top-center" });
                return; // Arrêter l'envoi des données
            }
        }
    }

    // Créez un tableau pour stocker toutes les données de sprint
    const allSprintsData = [];

    // Parcourez tous les sprints dans sprintData et ajoutez-les au tableau
    for (const currentSprint of sprintData.value) {
        allSprintsData.push({
            startDate: currentSprint.startDate,
            endDate: currentSprint.endDate,
            sprintEndType: currentSprint.sprintEndType
        });
    }

    // Envoyer les données à l'API
    try {
        // Envoyer les données à l'API et attendre la réponse
        const response = await fetchData(allSprintsData);
        // Vérifier si la réponse est réussie
        if (response.status === 201) {
            // Afficher un toast de succès si l'envoi est réussi
            formSubmitSuccess.value = true; // Marquer la soumission du formulaire comme réussie
            formSubmitted.value = true; // Marquer le formulaire comme soumis
            // Afficher un toast de succès si l'envoi est réussi
            toast.success('Le formulaire a été soumis avec succès !', { position: "top-center" });
            emit('update:formSubmitSuccess');

        } else {
            // Afficher un toast d'erreur si la réponse n'est pas réussie
            toast.error('Une erreur s\'est produite lors de l\'envoi du formulaire, il n\'y a pas de réponse du serveur', { position: "top-center" });
        }
    } catch (error) {
        // Afficher un toast d'erreur si l'envoi échoue
        toast.error('Une erreur s\'est produite lors de l\'envoi du formulaire.', { position: "top-center" });
    }
};

</script>
