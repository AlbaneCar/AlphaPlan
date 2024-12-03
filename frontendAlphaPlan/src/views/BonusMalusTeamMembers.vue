<template>
    <Suspense>

      <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG gap-4">

      <!-- Page des Bonus/Malus côté TM -->
      <Title>Gestion des bonus & malus - {{ equipeDetails.nom }}</Title>

      <!-- Sélection du sprint -->
      <Chooser v-if="sprints" :sprints="sprints" type="SPRINTS" v-model="sprint" class="items-center" />
      <div v-if="sprint.length == 0" class="absolute top-0 bottom-0 translate-x-1/4 translate-y-1/2 w-1/2 h-1/2 z-50">
      </div>

      <!-- Affichage de la progression, selon les décisions concernant les BM -->
      <div v-else>
        <div class="flex items-center justify-between">

          <!-- Avancement 1 : Choix des BM -->
          <div class="flex items-center" :class="{ 'text-gray-400 border-gray-400': advancment !== 2, 'text-blue-600 border-blue-600': advancment === 2 }">
            <div class="h-6 w-6 flex items-center justify-center rounded-full border-2" :class="{ 'border-gray-400': advancment !== 2, 'border-blue-600': advancment === 2 }">
              <span class="font-semibold">1</span>
            </div>
            <span class="ml-2 text-base font-medium" :class="{ 'text-gray-400': advancment !== 2, 'text-blue-700': advancment === 2 }">Choix des bonus & malus</span>
          </div>

          <!-- Avancement 2 : Validation des BM par les membres de l'équipe -->
          <div class="flex items-center" :class="{ 'text-gray-400 border-gray-400': advancment !== 3, 'text-blue-600 border-blue-600': advancment === 3 }">
            <div class="h-6 w-6 flex items-center justify-center rounded-full border-2" :class="{ 'border-gray-400': advancment !== 3, 'border-blue-600': advancment === 3 }">
              <span class="font-semibold">2</span>
            </div>
            <span class="ml-2 text-base font-medium" :class="{ 'text-gray-400': advancment !== 3, 'text-blue-700 dark:text-white': advancment === 3 }">Validation par l'équipe</span>
          </div>

          <!-- Avancement 3 : Validation par le référent de l'équipe -->
          <div class="flex items-center" :class="{ 'text-gray-400 border-gray-400': advancment !== 4, 'text-blue-600 border-blue-600': advancment === 4 }">
            <div class="h-6 w-6 flex items-center justify-center rounded-full border-2" :class="{ 'border-gray-400': advancment !== 4, 'border-blue-600': advancment === 4 }">
              <span class="font-semibold">3</span>
            </div>
            <span class="ml-2 text-base font-medium" :class="{ 'text-gray-400': advancment !== 4, 'text-blue-700 dark:text-white': advancment === 4 }">Validation par le référent</span>
          </div>

          <!-- Avancement 4 : Choix terminés -->
          <div class="flex items-center" :class="{ 'text-gray-400 border-gray-400': advancment !== 5, 'text-blue-600 border-blue-600': advancment === 5 }">
            <div class="h-6 w-6 flex items-center justify-center rounded-full border-2" :class="{ 'border-gray-400': advancment !== 5, 'border-blue-600': advancment === 5 }">
              <span class="font-semibold">4</span>
            </div>
            <span class="ml-2 text-base font-medium" :class="{ 'text-gray-400': advancment !== 5, 'text-blue-700 dark:text-white': advancment === 5 }">Décision finale</span>
          </div>

        </div>
      </div>


      <!-- Affichage si le référent est connecté -->
      <div v-if="idUtilisateurConnecte == referent">

        <!-- Image si aucun sprint créé -->
        <div v-if="sprint.length == 0" class="absolute top-0 bottom-0 translate-x-1/4 translate-y-1/2 w-1/2 h-1/2 z-50">
            <img src="/no_sprints.svg" alt="No sprints" class="scale-75">
        </div>
        <div v-else class="flex flex-wrap">

            <!-- Tableau récapitulatif des BM et de leur validatiob -->
            <div v-if="advancment == 2 || advancment == 3 || advancment == 4 || advancment == 5" class="w-full my-8 p-2 disabled">
                <TableBM :advancment="advancment" :teamMembers="teamMembers" :teamId="teamId" @nobody="(valeur) => nobody = valeur" @update-totals="handleTotals" @update-TMBM="handleBMTM" />
            </div>
            <div class="w-full md:w-1/2 p-2">
              <div>
                <div v-if="advancment == 2" class="w-96 h-20 relative rounded-2xl border border-orange-400 bg-white">
                    <div class="w-96 h-20 rounded-2xl"></div>

                        <!-- Message indiquant que les BM sont en réflexion -->
                        <div class="absolute inset-y-0 flex items-center m-2">
                            <Validate color="#FB923D" :width="60" />
                            <span class="text-orange-400 text-sm font-CASlalomBold m-2">Les bonus/malus du sprint sont en cours de réflexion par les membres de l'équipe.</span>
                        </div>
                    </div>
                    <div v-if="advancment == 3" class="w-96 h-20 relative rounded-2xl border border-orange-400 bg-white">
                        <div class="w-96 h-20 rounded-2xl"></div>

                        <!-- Message indiquant que les BM sont en cours de validation -->
                        <div class="absolute inset-y-0 flex items-center m-2">
                            <Validate color="#FB923D" :width="60" />
                            <span class="text-orange-400 text-sm font-CASlalomBold m-2">Les bonus/malus du sprint ont été choisis. Ils sont en cours de validation</span>
                        </div>
                    </div>
                    <div v-if="advancment == 5" class="w-96 h-20 relative rounded-2xl border border-green-400 bg-white">
                        <div class="w-96 h-20 rounded-2xl"></div>

                        <!-- Message indiquant que les BM sont validés -->
                        <div class="absolute inset-y-0 flex items-center m-2">
                            <Validate color="#62E291" :width="60" />
                            <span class="text-green-400 text-sm font-CASlalomBold m-2">Les bonus/malus du sprint ont été validés par tous les membres de l’équipe.</span>
                        </div>
                    </div>
                    <div>

                        <!-- Valider les BM pour le référent -->
                        <Button v-if="advancment == 4" icon='validate' class="py-2 px-4 mr-2" @click="validateFinalBM"> Je valide les bonus/malus </Button>

                        <!-- Ne pas valider les BM -->
                        <ButtonError v-if="advancment == 4" icon='disagree' class="py-2 px-4 mt-4" @click="suppBM"> Je ne suis pas d'accord </ButtonError>
                    </div>
                </div>
            </div>

            <!-- Informations pratiques sur les BM -->
            <div class="w-full md:w-1/2 p-2">
                <span class="block font-CASlalomBold text-black text-xl text-justify">Informations sur les Bonus & Malus :</span>
                <span class="block text-neutral-400 text-sm font-normal font-Roboto text-justify"><em>En tant que référent de cette équipe, vous devez valider les bonus/malus qui ont été attribués par les membres du groupe. Si vous n'êtes pas d'accord avec les choix proposés, alors les bonus/malus devront à nouveau être décidés</em></span>
            </div>
        </div>
      </div>

      <div v-else>
        <div v-if="sprint.length == 0" class="absolute top-0 bottom-0 translate-x-1/4 translate-y-1/2 w-1/2 h-1/2 z-50">
            <img src="/no_sprints.svg" alt="No sprints" class="scale-75">
        </div>
        <div v-else class="flex flex-wrap">
          <div class="w-full p-2 mt-3">
          <TableBM :advancment="advancment" :teamMembers="teamMembers" :teamId="teamId" @nobody="(valeur) => nobody = valeur" @update-totals="handleTotals" @update-TMBM="handleBMTM" />
            <div class="flex justify-end items-center w-full">

              <!-- Affichage des totaux des Bonus et des Malus -->
              <div v-if="advancment == 2" class="flex gap-4 mt-4">
                  <div
                      class="uppercase font-CASlalomBold text-custom-lightblueAP text-xs flex bg-white rounded-md border border-gray-200 p-2">
                      Bonus donnés : {{ totalBonus }}
                  </div>
                  <div
                      class="uppercase font-CASlalomBold text-custom-red text-xs flex bg-white rounded-md border border-gray-200 p-2">
                      Malus donnés : {{ totalMalus }}
                  </div>
              </div>
            </div>
          </div>

            <!-- Espace commentaire pour justifier les choix concernant les BM -->
            <div class="w-full md:w-1/2 my-8 p-2">
                <span v-if="advancment == 1 || advancment == 2" class="block font-CASlalomBold text-black text-xl">Commenter votre choix :</span>
                <span v-if="advancment == 3 || advancment == 4 || advancment == 5" class="block font-CASlalomBold text-black text-xl">Commententaire associé</span>
                <textarea v-if="advancment == 1 || advancment == 2" v-model="comment" class="w-full px-4 py-2 mt-2 font-CASlalomBold text-neutral-400 italic text-sm font-normal border border-gray-300 rounded-lg h-20 resize-none" placeholder="Si vous faîtes le choix d'accorder des Bonus & Malus, expliquez en quelques lignes les raisons qui ont motivées ces changements." :readonly="advancment == 1"></textarea>
                <textarea v-if="advancment == 3 || advancment == 4 || advancment == 5" class="w-full px-4 py-2 mt-2 font-CASlalomBold text-neutral-400 italic text-sm font-normal border border-gray-300 rounded-lg h-20 resize-none" :placeholder="commentBMvalide" readonly></textarea>
                <div>

                    <!-- Bouton pour enregistrer les choix d'un BM -->
                    <Button v-if="advancment == 2" icon="save" class="mt-4 py-2 px-4" @click="saveBM">Enregistrer les modifications</Button>
                    <Button v-if="advancment == 1 || advancment == 3" icon="save" class="mt-4 py-2 px-4 bg-gray-300 disabled">Enregistrer les modifications</Button>
                </div>
            </div>
            <div class="w-full md:w-1/2 my-8 p-2">

                <!-- Informations pratiques sur les BM -->
                <span class="block font-CASlalomBold text-black text-xl text-justify">Informations sur les Bonus & Malus :</span>
                <span class="block text-neutral-400 text-sm font-normal font-Roboto text-justify"><em>Afin de pouvoir valider les bonus et malus attribués, ces derniers doivent se compenser et ne pas dépasser plus de 4 points de différence par personne.</em></span>
                <span class="block font-CASlalomBold text-black text-xl mt-4">Validation des Bonus & Malus :</span>
                <span class="block text-neutral-400 text-sm font-normal font-Roboto text-justify"><em>En tant que membre de cette équipe, vous devez tous valider les bonus/malus qui ont été attribués. Sans validation de tous les membres, les bonus/malus ne seront pas attribués.</em></span>
                <span class="block text-neutral-400 text-sm font-normal font-Roboto mt-2"><em>Une fois la validation de tous les membres effectuée, les bonus/malus de ce sprint ne seront plus modifiables.</em></span>
                <div>

                    <!-- Choisir de valider ou non les choix ayant été faits -->
                    <Button v-if="advancment == 3" icon='validate' class="py-2 px-4 mt-4 mr-2" @click="validateBM"> Je valide les bonus/malus </Button>
                    <ButtonError v-if="advancment == 3" icon='disagree' class="py-2 px-4 mt-4" @click="suppBM"> Je ne suis pas d'accord </ButtonError>
                    <Button v-if="advancment == 1 || advancment == 2" icon='validate' class="py-2 px-4 mt-4 bg-gray-300 disabled"> Je valide les bonus/malus </Button>
                </div>
                <div>
                    <div v-if="advancment == 4" class="w-96 h-20 relative rounded-2xl border border-orange-400 mt-4 bg-white">
                        <div class="w-96 h-20 rounded-2xl"></div>
                        <div class="absolute inset-y-0 flex items-center m-2">
                            <Validate color="#FB923D" :width="60" />
                            <span class="text-orange-400 text-sm font-CASlalomBold m-2">Les bonus/malus du sprint sont en cours de validation par le référent de l'équipe.</span>
                        </div>
                    </div>
                    <div v-if="advancment == 5" class="w-96 h-20 relative rounded-2xl border border-green-400 mt-4 bg-white">
                        <div class="w-96 h-20 rounded-2xl"></div>
                        <div class="absolute inset-y-0 flex items-center m-2">
                            <Validate color="#62E291" :width="60" />
                            <span class="text-green-400 text-sm font-CASlalomBold m-2">Les bonus/malus du sprint ont été validés par tous les membres de l’équipe.</span>
                        </div>
                    </div>
                </div>
            </div>
        </div>
      </div>
    </div>
</Suspense>
</template>
<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute } from 'vue-router';
import { getTeamInformations, getNoteTeBM, getTeamName, addNoteTEBM, modifyNoteEleve, NotifSuppBM, getBonusMalusList, getBMNote, addBonusMalus, getNbBMValides, validateBonusMalus, suppBonusMalus } from '../services/BonusMalusTMService';
import { useToast } from 'vue-toastification';
import Title from '../components/Title.vue';
import TableBM from '../components/DataTable/TableBM.vue';
import TotalBM from '../components/TotalBM.vue';
import Button from '../components/Buttons/Button.vue';
import ButtonError from '../components/Buttons/ButtonError.vue';
import Validate from '../assets/icons/Validate.vue';
import DropdownSprints from '../components/DropdownSprints.vue';
import Chooser from '../components/Chooser/Chooser.vue'
import { getAllSprints } from '../services/SprintService';

console.log("teamId", localStorage.getItem('teamId'));
console.log("roles", localStorage.getItem('roles'));

const nobody = ref(null);
const route = useRoute();
const toast = useToast();
const teamId = ref(localStorage.getItem('teamId'));
const comment = ref('');
const commentBMvalide = ref('');

const teamMembers = ref([]);
const equipeDetails = ref([]);
const referent = ref(0);

const totalBonus = ref(0);
const totalMalus = ref(0);
const countNoNote = ref(0);
const countBM = ref(0);
const countNbValidations = ref(0);
const countNbIsValide = ref(0);
const nbMembers = ref(0);
const advancment = ref(0);
const Bvalide = ref(0);
const Mvalide = ref(0);

const idUtilisateurConnecte = ref(localStorage.getItem("user_id"));
const sprint = ref([]);
let sprintId = null;

const sprints = ref(null);

async function getSprintsData() {
  sprints.value = await getAllSprints();
}

const updateSprint = (newValue) => {
  if (newValue.length > 0) {
    sprintId = newValue[0].id;
  }
}

// MAJ du nombre de bonus et de malus total
const handleTotals = (type, amount) => {
  if (type === 'bonus') {
    totalBonus.value += amount;
  } else if (type === 'malus') {
    totalMalus.value += amount;
  }
}

// Enregistrement du nombre de BM par membre
const handleBMTM = (memberId, bonusMalus) => {
  const memberToUpdate = teamMembers.value.find(member => member.id === memberId);

  if (memberToUpdate) {
    memberToUpdate.bonusMalus = bonusMalus;
  }
};

// Sauvegarde des BM
const saveBM = () => {
  if (totalBonus.value === totalMalus.value) {
    // Message d'erreur s'il n'y a pas de commentaire associé
    if (comment.value.trim() === '') {
      toast.error("Veuillez ajouter un commentaire pour justifier ces choix.", {
        position: "top-center",
      });
    } else {
      addBonusMalus(teamMembers.value, comment.value.trim(), idUtilisateurConnecte.value)
        .then(() => {
          toast.success("Tous les bonus/malus ont été ajoutés avec succès.", {
            position: "top-center",
          });
          setTimeout(() => {
            loadData();
          }, 2000);
        })
        .catch(error => {
          console.error('Une erreur s\'est produite lors de l\'ajout des bonus/malus :', error);
        });
    }
  } else {
    // Message d'erreur si les BM ne sont pas équilibrés
    toast.error("Le bonus et malus ne sont pas équilibrés.", {
      position: "top-center",
    });
  }
};

// Fonction pour valider les BM au final, et donc publier la note associée
const validateFinalBM = () => {
  modifyNoteEleve(teamMembers.value, idUtilisateurConnecte.value)
    .then((success) => {
      if (success) {
        toast.success("Vous venez de valider les bonus malus !", {
          position: "top-center",
        });
        setTimeout(() => {
          loadData();
        }, 2000);
        console.log("loading");
      } else {
        toast.error("Une erreur s'est produite :(", {
          position: "top-center",
        });
      }
    })
    .catch(error => {
      toast.error(error.message, {
        position: "top-center",
      });
    });
}

// Validation de chaque membre
const validateBM = () => {

  validateBonusMalus(teamMembers.value, idUtilisateurConnecte.value)
    .then((success) => {
      if (success) {
        toast.success("Vous venez de valider les bonus malus !", {
          position: "top-center",
        });
        setTimeout(() => {
          loadData();
        }, 2000);
      } else {
        toast.error("Vous avez déjà validé ce Bonus Malus.", {
          position: "top-center",
        });
      }
    })
    .catch(error => {
      toast.error(error.message, {
        position: "top-center",
      });
    });

};

// Désaccord d'un membre et donc suppression des BM
const suppBM = () => {
  suppBonusMalus(teamMembers.value)
  NotifSuppBM(teamMembers.value, idUtilisateurConnecte.value)
    .then(() => {
      toast.success("Les bonus et malus ont été supprimés !", {
        position: "top-center",
      });
      setTimeout(() => {
        loadData();
      }, 2000);
    })
    .catch(error => {
      toast.error("Une erreur s'est produite" + error, {
        position: "top-center",
      });
    });
};

const loadData = async () => {
  try {
    if (sprints.value !== null && teamId.value !== null) {
      totalBonus.value = 0;
      totalMalus.value = 0;
      Bvalide.value = 0;
      Mvalide.value = 0;
      sprintId = sprint.value[0].id;

      const resp = await getTeamName(teamId.value);
      equipeDetails.value = resp;
      referent.value = equipeDetails.value.utilisateur.id;

      const response = await getTeamInformations(teamId.value);
      const teamMembersWithNotes = await Promise.all(response.map(async (member) => {

        const noteTeBM = await getNoteTeBM(member.id, sprintId);
        member.note_tebm = noteTeBM ?? -1;

        const bmNote = await getBMNote(member.note_tebm.id);
        member.bm_note = bmNote ?? -1;

        const nbBMValides = await getNbBMValides(member.id, sprintId, teamId.value, "TE_BM");
        member.nbBMValides = nbBMValides ?? -1;

        return member;
      }));

      teamMembers.value = teamMembersWithNotes;
      countNoNote.value = teamMembers.value.filter(member => member.note_tebm == -1).length;
      countBM.value = teamMembers.value.filter(member => member.bm_note != -1).length;
      nbMembers.value = teamMembers.value.length;

      //const { bonusMalusList, nbResponses } = await getBonusMalusList(teamId, sprint.value);
      if (countNoNote.value != 0) {
        await addNoteTEBM(teamMembers.value, sprintId, 0);
        location.reload();
      } else if (countBM.value != nbMembers.value) {
        advancment.value = 2; //pas de BM attribués, possibilité de les établir
      } else {
        teamMembers.value.forEach(member => {
          member.bonusMalus = member.bm_note.valeur;
        });

        countNbValidations.value = teamMembers.value.filter(member => member.nbBMValides == nbMembers.value).length;
        const member = teamMembers.value.find(member => member.id == idUtilisateurConnecte.value);
        if (member && member.bm_note) {
          commentBMvalide.value = member.bm_note.commentaire;
        }

        if (countNbValidations.value == nbMembers.value) {
          countNbIsValide.value = teamMembers.value.filter(member => member.bm_note.valide == 1).length;
          console.log(countNbIsValide.value);
          if (countNbIsValide.value == nbMembers.value) {
            advancment.value = 5; //les BM ont été validés par l'encadrant
          } else {
            advancment.value = 4; //les BM ont été validés par l'équipe
          }
        } else {
          advancment.value = 3; //des BM ont été enregistrés - attente de validation
        }
      }

      teamMembers.value.forEach(member => {
        if (member.bm_note.valeur > 0) {
          Bvalide.value += member.bm_note.valeur;
        } else if (member.bm_note.valeur < 0) {
          Mvalide.value += member.bm_note.valeur;
        }
      });
    }
  } catch (error) {
  }
};

loadData();
getSprintsData();

watch(() => sprint.value, async (value) => {
  if (sprint.value[0].id != null) {
    loadData();
  }

});
</script>

<style scoped>
.disabled {
  pointer-events: none;
}
</style>