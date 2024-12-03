<template>
    <div  v-if="teamMembers && teamMembers.length != 0" class="flex flex-col bg-custom-sideBarBG h-screen w-full p-8 gap-4">

      <!-- Titre de la page -->
      <Title>Ordre de passage</Title>
      <p class="text-sm text-custom-darkblueAP font-Roboto">
        Vous pouvez choisir l'ordre de passage des étudiants de votre equipe ainsi que le sprint de la présentation.
      </p>

      <!-- Message d'information sur l'ordre de passage déjà enregistré -->
      <Message v-if="OrdrePassageInfo" severity="info"  :closable="false" class="text-lg border m-0">{{ OrdrePassageInfo }}</Message>

      <!-- Dropdown pour choisir le sprint -->
      <Chooser v-if="sprints" :sprints="sprints" type="SPRINTS" v-model="sprint" class="items-center" />

      <!-- Si l'équipe a des membres -->
      <div class="justify-center p-4 mb-4 mt-6 m-6">
        <div class="w-1/2 bg-white p-4 rounded shadow mx-auto flex flex-col">
          <VueDraggableNext v-model="teamMembers" group="people" class="flex-1 overflow-auto text-center">
            <div v-for="(member, index) in teamMembers" :key="index" class="p-2 bg-gray-200 rounded-md shadow mb-2">
              {{ member?.nom }} {{ member?.prenom }}
            </div>
          </VueDraggableNext>
          <button @click="submitForm"
            class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-2 mb-2 font-CASlalomBold hover:bg-custom-darkblueAP duration-300  hover:transition-colors disabled:bg-gray-300 disabled:text-gray-600">
            Publier l'ordre de passage
          </button>
        </div>
      </div>
    </div>
    
    <!-- Si l'équipe n'a pas de membres -->
    <div  v-else-if="teamMembers && teamMembers.length == 0" class="flex-1 p-6 flex flex-col h-screen overflow-y-auto">
      <div class="flex flex-1 justify-center items-center">
          <div class="flex flex-row justify-center items-center">
            <Illustration nameImage="nothing3.svg" width="300" />
            <Title>Aucun étudiant dans cette équipe !</Title>
          </div>
      </div>
    </div>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { VueDraggableNext } from 'vue-draggable-next';
import { getTeamById } from '../services/PresentationFormService';
import { fetchSprint } from '../services/SprintService';
import { fetchOrdrePassage, CreateUpdateOrdrePassage } from '../services/OrdrePassageService';
import Title from '../components/Title.vue';
import Illustration from '../components/NoDataIllustration/Illustration.vue';
import { useToast } from 'vue-toastification';
import DropdownSprints from '../components/DropdownSprints.vue';
import Message from 'primevue/message';
import { getAllSprints } from '../services/SprintService';
import Chooser from '../components/Chooser/Chooser.vue';

const toast = useToast();
const route = useRoute();
const teamId = route.params.teamId; // Get team id from URL parameter
const currentUserId = localStorage.getItem("user_id");
const teamMembers = ref([]);
const sprint = ref([]);
const sprints = ref(null);
const OrdrePassageInfo = ref();
console.log('OrdrePassageInfo:', OrdrePassageInfo);
onMounted(async () => {
  sprint.value = await fetchSprint();
  const ordrePassage = await fetchOrdrePassage( sprint.value[0].id, teamId);
  console.log(ordrePassage);
  if (ordrePassage.error === null) {
    teamMembers.value = ordrePassage.ordre;
    console.log('teamMembers:', teamMembers.value);
    OrdrePassageInfo.value = `A noter : Un ordre de passage pour l'equipe ${ordrePassage.equipe.nom} lors du sprint ${ordrePassage.sprint.name} a déjà été enregistré par ${ordrePassage.auteur.prenom} ${ordrePassage.auteur.nom} le ${ordrePassage.dateCreation}.`;
  } else {
    teamMembers.value = await getTeamById(teamId);
    console.log('teamMembers:', teamMembers.value);
  }
});


watch(sprint, async (newSprint, oldSprint) => {
  console.log(newSprint);
  if (newSprint !== oldSprint) {
    const ordrePassage = await fetchOrdrePassage( sprint.value[0].id, teamId);
    console.log(ordrePassage);
    if (ordrePassage.error === null) {
      teamMembers.value = ordrePassage.ordre;
      console.log('teamMembers:', teamMembers.value);
      OrdrePassageInfo.value = `A noter : Un ordre de passage a déjà été enregistré pour l'equipe ${ordrePassage.equipe.nom} lors du sprint ${ordrePassage.sprint.name} par ${ordrePassage.auteur.prenom} ${ordrePassage.auteur.nom} le ${ordrePassage.dateCreation}.`;
    } else {
      teamMembers.value = await getTeamById(teamId);
      console.log('teamMembers:', teamMembers.value);
    }  
  }
});

const submitForm = async () => { 
  const ordres = teamMembers.value.map((member) =>  member.id );
  console.log(ordres);
  console.log("sprint", sprint.value);
  try {
    console.log('create', sprint.value[0].id, teamId, currentUserId, ordres);
  await CreateUpdateOrdrePassage(sprint.value[0].id, teamId, currentUserId, ordres);
  toast.success('Ordre de passage enregistré avec succès !', { position: 'top-center' });
  } catch (error) {
    console.error('Error while submitting form data:', error);
    toast.error('Erreur lors de l\'enregistrement de l\'ordre de passage !', { position: 'top-center' });
  }
};

async function getSprintsData() {
    sprints.value = await getAllSprints();
}

getSprintsData();
</script>