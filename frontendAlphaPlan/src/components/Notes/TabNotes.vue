<template>
  <div v-if="notes !== undefined" class="flex flex-col shadow-sm border rounded-md w-full h-fit bg-white">

    <!-- Header du tableau -->
    <div class="flex-row flex w-full items-center justify-between border-b rounded-t-md text-sm font-CASlalomBold bg-gray-100 text-gray-700">
      <div class="flex w-1/5 px-6 py-3">Prénom NOM</div>
      <div class="flex flex-row w-4/5 justify-between">
        <div v-for="tag in tags" class="flex max-w-[7.69%] min-w-[7.69%] justify-center px-6 py-3"
        v-tooltip.top="{value: NAMES[tag], pt: {text: 'text-xs text-center'}}">
          {{ tag }}
        </div>
      </div>
    </div>

    <!-- Lignes du tableau -->
    <RowTabNotes v-for="note in notes" :class="{'text-gray-500': !roles.includes('TEAM_MEMBER'), 'bg-blue-50': roles.includes('TEAM_MEMBER') && (nomUser == note.nom && prenomUser == note.prenom)}"
    :dataEleve="[note]" :sprintId="sprintId" :restriction="roles.includes('TEAM_MEMBER') && !(nomUser == note.nom && prenomUser == note.prenom)"/>

    <!-- Footer du tableau -->
    <div class="flex-row flex px-6 py-3 w-full justify-between items-center text-xs bg-gray-100 rounded-b-md">
      <!-- Bouton de détail des notes -->
      <div v-tooltip.bottom="{value: 'Information sur le calcul', pt: {text: 'text-xs text-center'}}">
        <Info class="cursor-pointer h-fit fill-gray-100 text-gray-700" @click="openDialog"/>
      </div>
      <Dialog v-model:visible="visibleCalcul" modal header="Détails des calculs" class="w-fit h-fit">
        <div class="flex flex-col w-fit h-fit justify-center items-center gap-3">
            <div class="border border-gray-100 bg-white shadow-sm w-full h-28 p-8 justify-center flex items-center rounded-md hover:bg-gray-50">
                <MathEquation :equation="'TeWo = \\dfrac{(1 \\times PrMa) + (1 \\times TeSo) + (1 \\times SpCo) + (1 \\times SuPr)}{4}'"/>
            </div>
            
            <div class="flex flex-row justify-between items-center w-full h-fit gap-3">
                <div class="border border-gray-100 bg-white shadow-sm w-fit h-28 p-8 flex justify-center items-center rounded-md hover:bg-gray-50">
                <MathEquation :equation="'InSp = TeWo + TeBm + SsBm'"/>
                </div>

                <div class="border border-gray-100 bg-white shadow-sm w-fit h-28 p-8 justify-center flex items-center rounded-md hover:bg-gray-50">
                <MathEquation :equation="'InPr = \\dfrac{(2 \\times SsPr) + (1 \\times OtPr)}{3}'"/>
                </div>
            </div>

            <div class="border border-gray-100 bg-white shadow-sm w-full h-28 p-8 flex justify-center items-center rounded-md hover:bg-gray-50">
                <MathEquation :equation="'IgSp = (0.7 \\times InSp) + (0.3 \\times InPr)'"/>
            </div>
        </div>
      </Dialog>
      <!-- Bouton d'export -->
      <div>
        <div v-tooltip.bottom="{value: 'Exporter les notes', pt: {text: 'text-xs text-center'}}">
          <FolderUp v-if="!roles.includes('TEAM_MEMBER')" class="cursor-pointer h-fit fill-gray-100 text-gray-700" @click.prevent="exportXLSX"/>
        </div>
      </div>
    </div>
  </div>

  <!-- Spinner de chargement -->
  <div v-else-if="loading" class="flex justify-center items-center w-full h-full">
    <ProgressSpinner />
  </div>

  <!-- Message d'erreur si les données ne sont pas chargées -->
  <NoDataIllusatration v-else path="no_sprints.svg" message="Oups, on dirait qu'il n'a pas de notes à afficher pour le moment !"/>
</template>

<script setup>
import { defineProps, watch, onMounted } from 'vue';
import { ref } from 'vue';
import { exportXLSX } from '../../services/ExportNotesService.js';
import { fetchNotesEquipe } from '../../services/NotesService.js';
import { Info, FolderUp } from 'lucide-vue-next';
import { useToast } from 'vue-toastification';

import RowTabNotes from './RowTabNotes.vue';
import Dialog from 'primevue/dialog';
import NoDataIllusatration from '../NoDataIllustration/NoDataIllusatration.vue';
import ProgressSpinner from 'primevue/progressspinner';
import MathEquation from '../MathEquation.vue';


// Variables
const roles = buildRoleString(JSON.parse(localStorage.getItem('roles')));
const nomUser = localStorage.getItem('nom');
const prenomUser = localStorage.getItem('prenom');

let visibleCalcul = ref(false);
const loading = ref(true);
const equipeId = ref(undefined);
const sprintId = ref(undefined);
const notes = ref(undefined);
const toaster = useToast();

// Tableau des tag des notes
const tags = ["PrMa", "TeSo", "SpCo", "SuPr", "TeWo", "TeBm", "SsBm", "InSp", "SsPr", "OtPr", "InPr", "IgSp"];
const NAMES = {
    'PrMa': 'Gestion de projet',
    'TeSo': 'Solution technique',
    'SpCo': 'Conformité du sprint',
    'SuPr': 'Support de la présentation',
    'TeWo': 'Travail en équipe',
    'TeBm': 'Bonus/malus d\'équipe',
    'SsBm': 'Bonus/malus des encadrants',
    'InSp': 'Note personnelle de sprint',
    'SsPr': 'Support de la présentation accordée par les encadrants',
    'OtPr': 'Support de la présentation accordée par les autres équipes',
    'InPr': 'Note individuelle de la présentation',
    'IgSp': 'Note individuelle globale de sprint'
};

// Props
const props = defineProps({
  equipe: Array,
  sprint: Array
});

// Fonction pour charger les données
const loadData = async () => {

  // Si l'utilisateur est un élève, on récupère son équipe
  if (roles.includes('TEAM_MEMBER')) {
    // Sinon, on récupère l'équipe de l'utilisateur car il est membre d'une équipe
    equipeId.value = localStorage.getItem('teamId');
  }

  // Si l'équipe n'est pas sélectionnée, on ne charge pas les données
  if (equipeId.value === undefined) {
    return;
  }

  // Si les sprints ne sont pas sélectionnés, on ne charge pas les données
  if (sprintId.value === undefined) {
    return;
  }
  // On affiche le spinner de chargement
  loading.value = true;
  // On essaie de récupérer les notes
  try {
    const data = await fetchNotesEquipe(equipeId.value, sprintId.value);
    notes.value = data;
    // Si on est un élève, on met ses notes en premier
    if (roles.includes('TEAM_MEMBER')) {
      notes.value = notes.value.sort((a, b) => {
        if (a.nom === nomUser && a.prenom === prenomUser) return -1;
        if (b.nom === nomUser && b.prenom === prenomUser) return 1;
        return 0;
      });
    }
  } catch (error) {
    toaster.error('Erreur lors du chargement des données');
    notes.value = [];
  }
  // On arrête le spinner de chargement
  loading.value = false;
};

// Appel de la fonction loadData() à chaque fois que les props equipe et sprint changent
watch(() => props.equipe, async () => {
  if (props.equipe.length) equipeId.value = props.equipe[0];
  if (props.sprint.length) sprintId.value = props.sprint[0].id;
  await loadData();
});
watch(() => props.sprint, async () => {
  if (props.equipe.length) equipeId.value = props.equipe[0];
  if (props.sprint.length) sprintId.value = props.sprint[0].id;
  await loadData();
});

// Appel de la fonction loadData() au montage du composant
onMounted(async () => {
  if (props.equipe.length) equipeId.value = props.equipe[0];
  if (props.sprint.length) sprintId.value = props.sprint[0].id;
  await loadData();
});

// Fonction pour ouvrir le dialog
const openDialog = () => {
  visibleCalcul.value = true;
};

// Fonction pour construire une chaîne de caractères des rôles de l'utilisateur
function buildRoleString(roles) {
  let roleString = '';
  roles.forEach(role => {
      roleString += role + '/';
  });
  return roleString;
}

</script>