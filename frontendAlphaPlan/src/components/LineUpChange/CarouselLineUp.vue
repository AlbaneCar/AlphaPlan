<template>
    <div class="flex flex-row h-fit w-full justify-center items-center">
        <!-- Les différentes propositions de changement de line-up -->
        <div class="flex justify-center items-center h-fit" :class="{'w-full': lineUps.length == 0, 'w-1/2': lineUps.length > 0}">
            <Carousel v-model:page="activeIndex" :value="lineUps" :numVisible="1" :numScroll="1" :showIndicators="false" orientation="vertical" verticalViewPortHeight="120px" contentClass="flex align-items-center" class="w-full h-fit">
                <template #item="slotProps">
                    <div class="h-full min-w-full flex flex-row justify-center items-center gap-10 px-4">
                        <!-- Auteur de la proposition -->
                        <div class="w-1/3 border rounded-md shadow-sm flex flex-col justify-center items-center px-2 py-5">
                            <p class="text-center">{{ slotProps.data.auteur.nom + ' ' + slotProps.data.auteur.prenom }}</p>
                            <p class="italic font-bold">({{ ((userRole.includes('PROJECT_LEADER')) ? 'Origine : ': '') + slotProps.data.auteur.equipe.nom }})</p>
                        </div>
                        <ArrowLeftRight class="h-10 w-10"/>
                        <!-- Candidat -->
                        <div class="w-1/3 border rounded-md shadow-sm flex flex-col justify-center items-center px-2 py-5">
                            <p class="text-center">{{ slotProps.data.proposition.nom + ' ' + slotProps.data.proposition.prenom }}</p>
                            <p class="italic font-bold">({{ ((userRole.includes('PROJECT_LEADER')) ? 'Origine : ': '') + slotProps.data.proposition.equipe.nom }})</p>
                        </div>
                    </div>
                </template>
            </Carousel>
        </div>
        <!-- Les différentes intéractions possibles -->
        <div v-if="lineUps.length && ((teamState === 'PUBLIE' && userRole.includes('TEAM_MEMBER')) || teamState === 'GENERE')" class="w-2/5 h-fit flex flex-col justify-between gap-10 items-center">
            <!-- Jauge de validation -->
            <MeterGroup :value="lineUpRatio" :labelPosition="'start'"/>
            <!-- Les boutons -->
            <div class="flex flex-row justify-evenly items-center h-fit w-full">
                <X class="h-10 w-10 rounded-md hover:bg-red-500 border-2 border-red-500 p-1 hover:text-white transition-all ease-in-out duration-100"
                :class="{'bg-red-500 text-white ': activeValidation=='REFUSE', 'text-red-500 cursor-pointer': activeValidation!='REFUSE'}"
                @click="rejectChanges()"/>
                <Check class="h-10 w-10 rounded-md hover:bg-green-500 border-2 border-green-500 p-1 hover:text-white transition-all ease-in-out duration-100"
                :class="{'bg-green-500 text-white': activeValidation=='ACCEPTE', 'text-green-500 cursor-pointer': activeValidation!='ACCEPTE'}"
                @click="validateChanges()"/>
            </div>
        </div>
    </div>
</template>

<script setup>
import Carousel from 'primevue/carousel';
import MeterGroup from 'primevue/metergroup';

import { onMounted, ref, watch, defineProps } from 'vue';
import { applyChanges, deleteOf, getAll, getValidationsOf, updateValidation } from '../../services/LineUpService';
import { createNotification } from '../../services/NotificationService';
import { useToast } from 'vue-toastification';
import { Check, X, ArrowLeftRight } from 'lucide-vue-next';


// Props pour l'état des équipes 'teamState' avec comme valeur par défaut 'PREBUBLIE'
const props = defineProps({
    teamState: {
        type: String,
        default: 'PREBUBLIE'
    }
});


// Variables
const teamState = ref(props.teamState);
const toast = useToast();
const lineUps = ref([]);
const activeLineUp = ref(null);
const lineUpValidations = ref([]);
const lineUpStatus = ref('ATTENTE');
const lineUpRatio = ref([{ label: 'Taux de validation', value: 0 }]);
const activeIndex = ref(0);
const activeValidation = ref('');
let userId = localStorage.getItem("user_id");
let userTeam = localStorage.getItem("teamId");
let userRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));
const emit = defineEmits(['eventCarousel']);


// Fonction pour construire une chaîne de caractères des rôles de l'utilisateur
function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}


// Fonction permettant de récupérer les données
const loadData = async () => {
    let response = await getAll();
    // Si on est pas PL alors on a accès seulement aux propositions concernant son équipe
    lineUps.value = response.lineUps.map(lineUp => ({ ...lineUp, swapped: false }));
    if (!userRole.includes('PROJECT_LEADER')) {
        lineUps.value = lineUps.value.filter(lineUp => (lineUp.auteur.equipe.id === parseInt(userTeam) || lineUp.proposition.equipe.id === parseInt(userTeam)) && lineUp.auteur.id !== parseInt(userId));
    }
    if (lineUps.value.length) {
        activeLineUp.value = lineUps.value[0];
        lineUpStatus.value = activeLineUp.value.status;
        await loadValidationsData(activeLineUp.value.id);
        if (!userRole.includes('PROJECT_LEADER')) {
            let userChoice = lineUpValidations.value.filter(lineUpValidation => lineUpValidation.utilisateur.id === parseInt(userId));
            activeValidation.value = userChoice.at(0).status
        }
    }
    console.log(lineUpValidations.value);
}


async function loadValidationsData(lineUpId) {
    const response = await getValidationsOf(lineUpId);
    lineUpValidations.value = response.validations;
    let count = lineUpValidations.value.filter(lineUpValidation => lineUpValidation.status === 'ACCEPTE').length;
    lineUpRatio.value.at(0).value = (count / lineUpValidations.value.length)*100;
}


// Fonction pour appliquer définitivement un changement 
const validateChanges = async () => {
    if (userRole.includes('PROJECT_LEADER')) {
        // Si le changement n'as pas déjà été fait, on l'applique
        if (!activeLineUp.value.swapped) {
            const response = await applyChanges(activeLineUp.value.id, true);
            if (response.status == 400) {
                toast.error(response.message);
                return;
            } else {
                toast.success(response.message);
                // On envoie les notifications
                lineUpValidations.value.forEach(async (validation) => {
                    await createNotification('CHANGEMENT_LINEUP', 'Un changement de line-up a été appliquée à votre équipe.', userId, validation.utilisateur.id);
                });
            }
        }
        // On récupère l'auteur en fonction du rôle de a personne connectée
        let auteurDelete = (userRole.includes('PROJECT_LEADER')) ? activeLineUp.value.auteur.id : userId;
        // Enfin, on supprime la proposition dans tout les cas
        const response = await deleteOf(auteurDelete, activeLineUp.value.id);
        if (response.status == 400) {
            toast.error(response.message);
        }
    } else {
        if (activeValidation.value != 'ACCEPTE') {
            const response = await updateValidation(userId, activeLineUp.value.id, true);
            toast.info(response.message);
        }
    }
    emit('eventCarousel');
    // Dans tout les cas on recharge les données
    await loadData();
}


// Fonction pour rejeter définitivement les modifications
const rejectChanges = async () => {
    if (userRole.includes('PROJECT_LEADER')) {
        // Si le changement n'as pas déjà été fait, on l'applique
        if (activeLineUp.value.swapped) {
            const response = await applyChanges(activeLineUp.value.id, true);
            if (response.status == 400) {
                toast.error("Il semble qu'une erreur se soit produite lors de la mise à jour la proposition !");
            }
        }
        // Enfin, on supprime la proposition dans tout les cas
        const response = await applyChanges(activeLineUp.value.id, false);
        if (response.status == 400) {
            toast.error(response.message);
        } else {
            toast.success(response.message);
            await createNotification('CHANGEMENT_LINEUP', 'Votre proposition de changement de line-up a été refusée.', userId, activeLineUp.value.auteur.id);
        }
    } else {
        if (activeValidation != 'REFUSE') {
            const response = await updateValidation(userId, activeLineUp.value.id, false);
            toast.info(response.message);
        }
    }
    emit('eventCarousel');
    // Dans tout les cas on charge les données puis on envoit une notification
    await loadData();
}


// On charge les données à l'initialisation du composant
onMounted(async () => {
    await loadData();
});


// Watcher pour l'index actif du carousel
watch(activeIndex, async (newIndex) => {
    activeIndex.value = newIndex;
    activeLineUp.value = lineUps.value[newIndex];
    lineUpStatus.value = activeLineUp.value.status;
    active.value = activeLineUp.value.swapped;
    await loadValidationsData(activeLineUp.value.id);
    if (!userRole.includes('PROJECT_LEADER')) {
        let userChoice = lineUpValidations.value.filter(lineUpValidation => lineUpValidation.utilisateur.id === parseInt(userId));
        activeValidation.value = userChoice.at(0).status
    }
});
</script>