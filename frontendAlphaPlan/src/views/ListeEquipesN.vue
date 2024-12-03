<script setup>
import Title from '../components/Title.vue'
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import { fetchEquipes } from '../services/EquipesService.js';
import { getValidationEquipes } from '../services/CreerEquipesService';
import SignalementForm from '../components/SignalementForm.vue';
import { fetchOLPL } from '../services/EquipesService.js';
import { submitFormData } from '../services/PresentationFormService';
import { postValidationEquipes } from '../services/CreerEquipesService';
import { ref, watch } from 'vue';
import { onMounted } from 'vue';
import TableMembers from '../components/DataTable/TableMembers.vue';
import Button from '../components/Buttons/Button.vue';
import { Handshake } from 'lucide-vue-next';
import { CircleCheckBig } from 'lucide-vue-next';
import { X } from 'lucide-vue-next';
import { MessageCircleWarning } from 'lucide-vue-next';
import Illustration from '../components/NoDataIllustration/Illustration.vue';
import { getEquipesMoyennes } from '../services/NotesService.js';
import LineUpOverview from '../components/LineUpChange/LineUpOverview.vue';


const equipesData = ref(null);
const isSS = ref(false);
const hasValidated = ref(false);
const etatEquipes = ref(null);
const isSignalementFormVisible = ref(false);
const selectedEquipe = ref(null);
const allAverage = ref(null);


const openSignalementForm = (equipe) => {
  selectedEquipe.value = equipe;
  changeVisibility();
}

function changeVisibility() {
  isSignalementFormVisible.value = !isSignalementFormVisible.value;
}

//Fonction pour compter le nombre de bachelors ou E3e 
function countType(membres, type) {
  return membres.filter(member => member.typeUtilisateur === type).length;
}

function countGender(membres, gender) {
  return membres.filter(member => member.genre === gender).length;
}

function getAllAverage() {
  getEquipesMoyennes()
    .then((response) => {
      if(response.ok) {
        return response.json();
      }
    })
    .then((data) => {
      allAverage.value = data;
    })
}

function roundMoyenne(moyenne) {
  return Math.round(moyenne * 100) / 100;
}

//au lancement du composant
onMounted(async () => {
  const equipes = await fetchEquipes();
  equipesData.value = equipes;
  console.log("Chargement des équipes", equipesData.value);
  const roles = JSON.parse(localStorage.getItem('roles'));
  roles.forEach(role => {
    if (role === "SUPERVISING_STAFF") {
      isSS.value = true;
      return;
    }
  });

  const validations = await getValidationEquipes();
  validations.forEach((item) => {
    if (item.utilisateur.id === userId.value) {
      hasValidated.value = true;
      return;
    }
  });

  try {
  if(etatEquipes.value === null && equipesData.value.length > 0){
    etatEquipes.value = equipesData.value[0].etatEquipes;
  }
    
  } catch (error) {
    console.table(error);
    console.error('Erreur lors de la récupération des équipes, des étudiants ou des détails du rôle:', error);
  }

  getAllAverage();
});

const postValidation = async () => {
  const OLPL = await fetchOLPL();
  for (let i = 0; i < OLPL.length; i++) {
    const OLPLId = OLPL[i].id;
    const message = "Je viens de valider la composition des équipes proposée";
    const formData = {
      message,
      type: 'EQUIPES',
      envoyeur: { "id": localStorage.getItem('user_id') },
      receveur: { "id": OLPLId }
    };
    try {
      await submitFormData(formData);
    } catch (error) {
      console.error(error);
    }
  }
  await postValidationEquipes(userId.value);
  hasValidated.value = true;
}

const moyenneEquipe = (equipeId) => {
  if(allAverage.value) {
    const moyenneEquipe = allAverage.value.filter((item) => item.idEquipe=== equipeId);
    return moyenneEquipe.map((item) => {
      item.moyenne == null ? item.moyenne = 0 : item.moyenne;
      return item;
    })
  }
}

const deleteValidation = async () => {
  const OLPL = await fetchOLPL();
  for (let i = 0; i < OLPL.length; i++) {
    const OLPLId = OLPL[i].id;
    const message = "Je viens de refuser la composition des équipes proposée";
    const formData = {
      message,
      type: 'EQUIPES',
      envoyeur: { "id": localStorage.getItem('user_id') },
      receveur: { "id": OLPLId }
    };
    try {
      await submitFormData(formData);
    } catch (error) {
      console.error(error);
    }
  }
  for (let i = 0; i < equipesData.value.length; i++) {
    const staffId = equipesData.value[i].utilisateur.id;
    const message = "Je viens de refuser la composition des équipes proposée";
    const formData = {
      message,
      type: 'EQUIPES',
      envoyeur: { "id": localStorage.getItem('user_id') },
      receveur: { "id": staffId }
    };
    try {
      await submitFormData(formData);
    } catch (error) {
      console.error(error);
    }
  }
  try {
    await deleteValidationEquipes();
  } catch (error) {
  }
  toast.success('Les équipes ont bien été refusée !', { position: 'top-center' });

  etatEquipes.value = 'GENERE';
};

const userId = ref(localStorage.getItem('user_id'))
userId.value = parseInt(userId.value, 10);


// On définit une variable de contrôle pour écouter les évènements
const eventData = ref(null);


// Fonction permettant de changer la donnée recevant les modifications dues à l'évènement
function handleCustomEvent(data) {
  eventData.value = data;
}

// Watcher permettant de recharger les données suite à un évènement
watch(eventData, (newVal) => {
  console.log('Le watcher a détecté un changement :', newVal);
});

</script>

<template>
  <Suspense>
    <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG gap-4">

      <!-- Titre et description de la page -->
      <div v-if="etatEquipes === 'PUBLIE' || isSS" class="flex flex-col">
        <Title>Liste des Équipes {{ etatEquipes === 'PREPUBLIE' ? '- [PRֻÉ-PUBLICATION]' : '' }}</Title>
      </div>

      <!-- Boutons d'actions -->
      <div class="w-full h-fit flex flex-col gap-4">

        <!-- Boutons de validation des équipes -->
        <div class="flex w-full justify-end items-center">
          <div v-if="isSS && !hasValidated && etatEquipes === 'PREPUBLIE'"
            class="flex bg-white px-3 py-2 rounded-md shadow-sm border border-gray-200 gap-8 items-center font-Roboto">
            <div class="flex justify-start items-center gap-2">
              <Handshake class="w-6 h-6 text-custom-darkblueAP" />
              <p class="font-CASlalomBold">Validation des équipes</p>
            </div>
            <div class="flex justify-end gap-3">
              <Button classes="px-2 py-1" @click="postValidation()">
                <div class="flex gap-2 items-center">
                  <CircleCheckBig class="w-4 h-4 text-white" />
                  <p>Valider</p>
                </div>

              </Button>
              <Button classes="px-2 py-1" @click="deleteValidation()">
                <div class="flex gap-2 items-center">
                  <X class="w-4 h-4 text-white" />
                  <p>Refuser</p>
                </div>
              </Button>
            </div>
          </div>
        </div>

        <!-- Vue d'ensemble des équipes -->
        <div>
          <LineUpOverview v-if="equipesData && etatEquipes === 'PUBLIE'" :teamState="etatEquipes"
          @eventLineUp="handleCustomEvent"/>
        </div>

        <!-- Accordion des équipes -->
        <Accordion v-if="(etatEquipes === 'PUBLIE' || isSS) && equipesData" :multiple="true" :activeIndex="0"
          class="bg-custom-sideBarBG flex flex-col gap-4">
          <AccordionTab v-for="equipe in equipesData" :header="equipe.nom">
            <div class="flex justify-start mb-4">
              <div
                class="bg-custom-sideBarBG border border-gray-200 shadow-sm rounded-md px-2 py-1 text-xs text-gray-700 font-CASlalomRegular flex justify-center items-center">
                <span class="font-bold">RÉFÉRENT :&nbsp;</span>{{ equipe.utilisateur.nom }} {{
                  equipe.utilisateur.prenom }}
              </div>
            </div>
            <TableMembers :equipe="equipe" />
            <div class="flex justify-between mt-4">
              <div class="flex gap-4">
                <div
                  class="bg-custom-sideBarBG border border-gray-200 shadow-sm rounded-md px-2 py-1 text-xs text-gray-700 font-CASlalomBold flex justify-center items-center"
                  v-tooltip.top="{
                    value: `Ratio Homme/Femme`,
                    pt: {
                      text: 'text-xs px-2 py-1'
                    }
                  }">
                  H/F : {{ countGender(equipe.membres, 'HOMME') }}/{{ countGender(equipe.membres,
                    'FEMME') }}
                </div>

                <div
                  class="bg-custom-sideBarBG border border-gray-200 shadow-sm rounded-md px-2 py-1 text-xs text-gray-700 font-CASlalomBold flex justify-center items-center"
                  v-tooltip.top="{
                    value: `Ratio E3E/Bachelor`,
                    pt: {
                      text: 'text-xs px-2 py-1'
                    }
                  }">
                  E3E/B : {{ countType(equipe.membres, 'E3E') }}/{{ countType(equipe.membres,
                    'BACHELOR') }}
                </div>

                <div
                  class="bg-custom-sideBarBG border border-gray-200 shadow-sm rounded-md px-2 py-1 text-xs text-gray-700 font-CASlalomBold flex justify-center items-center"
                  v-tooltip.top="{
                    value: `Moyenne des notes antérieures de l'équipe`,
                    pt: {
                      text: 'text-xs px-2 py-1'
                    }
                  }">
                  MOYENNE : {{ roundMoyenne(equipe.moyenne) }}
                </div>


                <div
                  v-for="(moyenne, index) in moyenneEquipe(equipe.id)"
                  :key="moyenne.id"
                  class="bg-custom-sideBarBG border border-gray-200 shadow-sm rounded-md px-2 py-1 text-xs text-gray-700 font-CASlalomBold flex justify-center items-center"
                  v-tooltip.top="{
                    value: `Moyenne des notes de l'equipe pendant les differents sprints`,
                    pt: {
                      text: 'text-xs px-2 py-1'
                    }
                  }"
                >
                  MOYENNE SPRINT {{ index + 1 }} : {{ moyenne.moyenne === 0 ? 'N/A' : roundMoyenne(moyenne.moyenne) }}
                </div>



              </div>
              <div>
                <Button classes="px-2 h-full" @click="openSignalementForm(equipe)">
                  <div class="flex gap-2 items-center">
                    <MessageCircleWarning class="w-4 h-4 text-white" />
                    <p>Signaler</p>
                  </div>

                </Button>
              </div>
            </div>
          </AccordionTab>
        </Accordion>

        <div v-if="(etatEquipes != 'PUBLIE' && !isSS)"
          class="w-full h-full flex justify-center items-center">
          <div class="flex justify-center items-center p-16">
            <Illustration nameImage="Clock.svg" width="350" />
            <Title>Les équipes sont en cours de création...</Title>
          </div>
        </div>

      </div>
      
      <div class="h-0">
        <SignalementForm :equipe="selectedEquipe" :visibilite="isSignalementFormVisible"
          @update:visibilite="() => changeVisibility()" />
      </div>
    </div>
  </Suspense>
</template>


<style scoped>
:deep(.p-accordion-content) {
  background-color: white;
  border-radius: 0.375rem;
}

:deep(.p-accordion-tab) {
  background-color: white;
  border-radius: 0.375rem;
  border: 1px solid rgb(229 231 235);
}

:deep(.p-accordion-header-link) {
  background-color: white;
}
</style>