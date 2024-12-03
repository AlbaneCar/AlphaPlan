<template>
    <!-- Section permettant d'effectuer une proposition de changement de line-up -->
    <div class="w-3/4 h-fit flex flex-row justify-between items-center">
        <div class="h-fit w-3/4 flex flex-row justify-center items-center gap-10 px-4">
            <!-- Auteur de la proposition : élève connecté -->
            <div class="w-2/5 min-h-full border bg-gray-200 rounded-md shadow-sm flex flex-col justify-center items-center px-2 py-4">
                <p class="text-center">{{ nom + ' ' + prenom }}</p>
            </div>

            <!-- Icone de séparation -->
            <ArrowLeftRight class="h-10 w-10"/>

            <!-- Candidats (si pas choisit) -->
            <Dropdown v-if="myRequest == null" v-model="value" :options="propositions" optionLabel="label" optionGroupLabel="nom" optionGroupChildren="items" placeholder="Choisir un étudiant" filter
            class="w-2/5 border rounded-md shadow-sm px-1 py-2" inputClass="text-center">
                <template #optiongroup="slotProps">
                    <div class="flex align-items-center">
                        <div>{{ slotProps.option.label }}</div>
                    </div>
                </template>
            </Dropdown>
            <!-- Candidat (si déjà choisit) -->
            <div v-if="myRequest != null" class="w-2/5 min-h-full border bg-gray-200 rounded-md shadow-sm flex flex-col justify-center items-center px-2 py-4">
                <p class="text-center">{{ myRequest.proposition.nom + ' ' + myRequest.proposition.prenom }}</p>
            </div>
        </div>

        <!-- Bouton -->
        <div class="w-1/4 h-32 flex flex-row justify-evenly items-center">
            <ClipboardPlus v-if="myRequest == null" class="h-10 w-10 bg-custom-sideBarBackground text-white rounded-md p-2 hover:bg-custom-sideBarActiveBG cursor-pointer transition-all ease-in-out duration-100"
            @click="sendRequest()"/>
            <Trash2 v-else class="h-10 w-10 bg-custom-sideBarBackground text-white rounded-md p-2 hover:bg-custom-sideBarActiveBG cursor-pointer transition-all ease-in-out duration-100"
            @click="removeRequest()"/>
        </div>
    </div>
</template>

<script setup>
import { ArrowLeftRight, ClipboardPlus, Trash2 } from 'lucide-vue-next';
import { onMounted, ref, watch } from 'vue';
import { useToast } from 'vue-toastification';
import Dropdown from 'primevue/dropdown';
import { fetchEquipes } from '../../services/EquipesService';
import { createLineUp, deleteOf, getAllBy } from '../../services/LineUpService';
import { createNotification } from '../../services/NotificationService';


// Variables
const nom = localStorage.getItem("nom");
const prenom = localStorage.getItem("prenom");
let userId = localStorage.getItem("user_id");
let userTeam = localStorage.getItem("teamId");
const propositions = ref([])
const value = ref();
const selectedProposition = ref(null);
const myRequest = ref(null);
const toast = useToast();


// Fonction d'envoi de la proposition de changement de line-up
const sendRequest = async () => {
    if (selectedProposition.value != null) {
        const response = await createLineUp(userId, selectedProposition.value.id);
        toast.info(response.message);
        await loadMyRequests();
    }
}


// Fonction d'envoie de la requête de suppression de la proposition
const removeRequest = async () => {
    const response = await deleteOf(userId, myRequest.value.id);
    toast.info(response.message);
    // On remet a zéro
    myRequest.value = null;
    await loadPropositions();
}


// Requête permettant d'avoir les line-ups d'un auteur
const loadMyRequests = async () => {
    const response = await getAllBy(userId);
    if (response.lineUps.length) {
        myRequest.value = response.lineUps.at(0);
    }
}


// Requête pour récupérer la liste des étudiants
const loadPropositions = async () => {
    const response = await fetchEquipes();
    // On initialise un tableau temporaire pour recevoir les données filtrées
    let temp = [];
    // On va parcourir la réponse et ajouter les sections qui nous intéressent
    response.forEach(element => {
        if (element.id !== parseInt(userTeam)) {
            let equipe = {label: element.nom, items: []};
            element.membres.forEach(membre => {
                equipe.items.push({label: membre.nom + ' ' + membre.prenom, id: membre.id});
            });
            temp.push(equipe);
        }
    });
    console.log(temp);
    // On ajoute les données à la variable
    propositions.value = temp;
}


// Fonction pour mettre à jour la conversation sélectionnée
const updateSelectedProposition = (newConversation) => {
    selectedProposition.value = newConversation;
}


// On charge les données quand le composant est monté
onMounted(async () => {
    // Tout d'abord on va regarder si l'utilisateur est déjà auteur d'une proposition
    await loadMyRequests();
    if (myRequest.value == null) {
        await loadPropositions();
    }
});


// Watcher pour l'élément actif du Dropdown
watch(value, async (newValue) => {
    updateSelectedProposition(newValue);
});
</script>