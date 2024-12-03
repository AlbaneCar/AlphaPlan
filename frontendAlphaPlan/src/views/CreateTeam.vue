<template>
  <Suspense>
    <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG gap-4">

      <!-- Titre -->
      <Title>Gestion des équipes</Title>

      <!-- Contenu -->
      <div v-if="teamsGenerated" class="flex flex-col justify-between h-fit w-full gap-4">

        <!-- Changements de line-up -->
        <LineUpOverview v-if="teamsGenerated && etatEquipes !== 'PREPUBLIE'" :teamState="etatEquipes"
        @eventLineUp="handleCustomEvent"/>
        
        <!-- Boutons de publication -->
        <div class="flex justify-end w-full">
          <div class="flex gap-2">
            <Button
              v-if="teamsAmount != 0 && nbValidation < teamsAmount && etatEquipes !== 'PUBLIE' && etatEquipes !== 'GENERE'"
              @click="visible = true" classes="p-2" v-tooltip.top="{
                value: `La composition des équipes a été validée par ` + nbValidation + ` superviseur(s) `,
                pt: {
                  text: 'text-xs px-2 py-1'
                }
              }">Publier | <span class="text-xs">{{ nbValidation }} / {{ teamsAmount }}</span></Button>
            <Button
              v-if="teamsAmount != 0 && nbValidation === teamsAmount && etatEquipes !== 'PUBLIE' && etatEquipes !== 'GENERE'"
              @click="sendTeams('PUBLIE')" classes="p-2">Publier | {{ nbValidation }} / {{ teamsAmount }}</Button>
            <Button v-if="teamsAmount != 0 && etatEquipes === 'GENERE'" @click="sendTeams('PREPUBLIE')"
              classes="p-2">Pré-Publier</Button>
            <Button v-if="teamsAmount != 0 && etatEquipes !== 'GENERE'" @click="modifyTeams()"
              classes="p-2">Modifier</Button>
            <Button v-if="teamsGenerated" @click="resetTeams" classes="p-2">Réinitialiser</Button>
          </div>
          <div class="h-0">
            <Dialog v-model:visible="visible" modal header="Confirmer la publication" class="min-w-[500px]">
              <p class="mb-5">Attention ! Tous les staffs n'ont pas validés les équipes</p>
              <div class="text-center">
                <Button @click="sendTeams('PUBLIE')" classes="p-2 mr-8">Publier</Button>
                <Button @click="visible = !visible" classes="p-2">Annuler</Button>
              </div>
            </Dialog>
          </div>
        </div>
      </div>

      <!--Chargement de la page-->
      <div v-if="isLoading" class="flex justify-center items-center h-full">
        <div class="loader"></div>
      </div>
      <div class="h-full" v-else>

        <!--Si les équipes ne sont pas encore générées-->
        <div v-if="!teamsGenerated" class="flex flex-row justify-center items-center h-full w-full gap-20">
          <div class="flex flex-col gap-3">
            <Illustration nameImage="nothing2.svg" :width="200" class="m-auto" />
            <Title class="text-center">Aucune équipe n'a encore<br>été créée !</Title>
          </div>
          <div class="h-1/2 w-[1px] border border-gray-300"></div>
          <div class="">
            <FormParamTeams @update-teams-amount="updateTeamsAmount" @teams-generated="generateTeams" />
          </div>
        </div>

      <!--Si les équipes sont en génération-->
      <div v-if="teamsGenerated && teamsData && canModifyTeams" :key="'teamCards'" class="flex flex-wrap gap-[2%] justify-around">
        <CreateTeamCard v-for="(team, index) in teamsData" :key="index" :team="team" :index="index"
          :teamName="team.name" :team-staff="team.staff" @update:teamName="updateTeamName(index, $event)"
          @update:teamStaff="updateTeamStaff(index, $event)" @team-statistics-updated="handleTeamStatisticsUpdated" />
      </div>

      <!--Si les équipes sont en prépublication ou publiées-->
      <div v-else-if="etatEquipes === 'PREPUBLIE' || etatEquipes === 'PUBLIE'"
        class="flex flex-row justify-center items-start h-full w-full gap-20">
        <div class="flex flex-col gap-3 w-full h-fit justify-start">
          <div>
            <Accordion v-if="equipesData" :multiple="false" class="bg-custom-sideBarBG flex flex-col gap-2">
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
                    </div>
                  </div>
                </AccordionTab>
              </Accordion>
            </div>
          </div>
        </div>
        <div v-else-if="etatEquipes === 'PUBLIE'" class="flex mt-8 mx-auto w-1/3">
          <Button v-if="teamsAmount != 0" @click="modifyTeams()" classes="p-2 m-auto">Modifier</Button>
        </div>
      </div>
    </div>
  </Suspense>
</template>

<script setup>
import { ref, onMounted, computed, watch } from 'vue';
import FormParamTeams from '../components/FormParamTeams.vue';
import CreateTeamCard from '../components/CreateTeamCard.vue';
import Button from '../components/Buttons/Button.vue';
import Title from '../components/Title.vue';
import { useToast } from 'vue-toastification';
import { envoyerEquipes, getValidationEquipes, modifierEquipes, setRole, supprimerEquipes } from '../services/CreerEquipesService';
import { fetchEtudiants } from '../services/EtudiantService';
import { fetchMoyenne } from '../services/EtudiantNotesService';
import Illustration from '../components/NoDataIllustration/Illustration.vue';
import { deleteValidationEquipes } from '../services/CreerEquipesService';
import { submitFormData } from '../services/PresentationFormService';
import Dialog from 'primevue/dialog';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import TableMembers from '../components/DataTable/TableMembers.vue';
import { MessageCircleWarning } from 'lucide-vue-next';
import LineUpOverview from '../components/LineUpChange/LineUpOverview.vue';
import { fetchEquipes } from '../services/EquipesService';

const teamsAmount = ref(0);
const teamsData = ref([]);
const errorMessages = ref([]);
const toast = useToast();
const teamsGenerated = ref(false);
const etatEquipes = ref(null);
const nbValidation = ref(0);
const isLoading = ref(true);
const canModifyTeams = ref(true);
const visible = ref(false);
const equipesData = ref(null);


onMounted(async () => {
  const equipes = await fetchEquipes();
  equipesData.value = equipes;
  try {
    const etudiants = await fetchEtudiants() || [];
      if (etudiants[0].equipe !== null) {
        if (etudiants.length) {
          const teamsMap = {};
          for (const etudiant of etudiants) {
            const moyenneEtudiant = await fetchMoyenne(etudiant);
            const equipe = etudiant.equipe;
            if (!teamsMap[equipe.id]) {
              teamsMap[equipe.id] = {
                name: equipe.nom,
                staff: equipe.utilisateur,
                etat: equipe.etatEquipes,
                members: []
              };
              etatEquipes.value = teamsMap[equipe.id].etat;
            }
            teamsMap[equipe.id].members.push({
              id: etudiant.id,
              nom: etudiant.nom,
              prenom: etudiant.prenom,
              genre: etudiant.genre,
              typeUtilisateur: etudiant.typeUtilisateur,
              moyenne: moyenneEtudiant
            });
          }
          teamsData.value = Object.values(teamsMap);
          console.log("teamData :", teamsData.value);
          console.log("EtatEquipe : ", etatEquipes.value);
          teamsGenerated.value = true;
          teamsAmount.value = teamsData.value.length;
        }
        if(etatEquipes.value != 'GENERE')
          canModifyTeams.value = false;
        else
          canModifyTeams.value = true;

        const validations = await getValidationEquipes();
        nbValidation.value = validations.length;
      }
  } catch (error) {
    console.error(error);
  } finally {
    isLoading.value = false;
    console.log("EquipeEtat :", etatEquipes.value);
  }
});

const updateTeamsAmount = (amount) => {
  teamsAmount.value = amount;
};

const updateTeamName = (index, value) => {
  if (teamsData.value[index]) {
    teamsData.value[index].name = value;
  }
};

const updateTeamStaff = (index, value) => {
  if (teamsData.value[index]) {
    teamsData.value[index].staff = value;
  }
};

//Fonction pour compter le nombre de bachelors ou E3e 
function countType(membres, type) {
  return membres.filter(member => member.typeUtilisateur === type).length;
}

function countGender(membres, gender) {
  return membres.filter(member => member.genre === gender).length;
}

function roundMoyenne(moyenne) {
  return Math.round(moyenne * 100) / 100;
}


const handleTeamStatisticsUpdated = (team) => {
  const teamIndex = teamsData.value.findIndex(t => t.id === team.id);
  if (teamIndex !== -1) {
    teamsData.value[teamIndex] = { ...teamsData.value[teamIndex], ...team };
  }
};

const teams = computed(() => {
  if (teamsData.value) {
    return teamsData.value;
  } else {
    return [];
  }
});

const generateTeams = (teamsDataAPI) => {
  teamsData.value = [];
  for (let i = 0; i < teamsAmount.value; i++) {
    teamsData.value.push({
      members: teamsDataAPI[i],
      name: null,
      staff: null
    });
  }
  if (teamsAmount.value > 0)
    teamsGenerated.value = true;
  sendTeams('GENERE');
};

const resetTeams = async () => {
  teamsGenerated.value = false;
  teamsData.value = [];
  teamsAmount.value = 0;
  etatEquipes.value = null;
  try {
    await supprimerEquipes();
  } catch (error) {
  }
  try {
    nbValidation.value = 0;
    await deleteValidationEquipes();
  } catch (error) {
  }
  toast.success("Équipes réinitialisées !", { position: "top-center" });
};

const checkErrors = () => {
  errorMessages.value = [];

  // Vérification du nom de l'équipe
  const teamNames = new Set();
  teamsData.value.forEach(team => {
    if (!team.name || teamNames.has(team.name)) {
      errorMessages.value.push("Nom d'équipe invalide ou en double");
    } else {
      teamNames.add(team.name);
    }
  });

  // Vérification du staff
  const staffSet = new Set();
  teamsData.value.forEach(team => {
    if (team.staff === null || staffSet.has(team.staff.id)) {
      errorMessages.value.push("Le même staff pour plusieurs équipes");
    } else {
      staffSet.add(team.staff);
    }
  });

  // Vérification du nombre de membres dans les équipes
  for (let i = 0; i < teamsData.value.length - 1; i++) {
    const currentTeam = teamsData.value[i];
    const nextTeam = teamsData.value[i + 1];
    const difference = Math.abs(currentTeam.members.length - nextTeam.members.length);
    if (difference > 1) {
      errorMessages.value.push("Différence de membres supérieure à 1 entre deux équipes");
    }
  }

  // Afficher les erreurs uniques
  const uniqueErrors = new Set(errorMessages.value);
  uniqueErrors.forEach(errorMessage => {
    toast.error(errorMessage, {
      position: "top-center",
      timeout: false
    });
  });
};

const sendTeams = async (newEtatEquipes) => {
  if (newEtatEquipes !== 'GENERE')
    checkErrors();
  const formattedTeamsData = teamsData.value.map(team => ({
    members: team.members,
    name: (team.name) ? team.name : 'Équipe ' + (teamsData.value.indexOf(team) + 1),
    staff: team.staff || 'Staff'
  }));
  try {
    if (errorMessages.value.length === 0) {
      if (newEtatEquipes === 'GENERE')
        toast.success("Équipes générées !", { position: "top-center" });
      else if (newEtatEquipes === 'PREPUBLIE') {
        toast.success("Équipes pré-publiées !", { position: "top-center" });
        for (let i = 0; i < teamsData.value.length; i++) {
          const staffId = teamsData.value[i].staff.id
          const message = "Les équipes ont été pré-publiées, merci de les valider";
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
      }
      else{
        visible.value = false;
        toast.success("Équipes publiées !", { position: "top-center" });
      }
      etatEquipes.value = newEtatEquipes;
      if (etatEquipes.value != 'GENERE')
        canModifyTeams.value = false;
      else
        canModifyTeams.value = true;

      await envoyerEquipes(formattedTeamsData, newEtatEquipes);
    } else {
      console.error("Erreur lors de la publication des équipes.");
    }
  } catch (error) {
    console.error("Erreur lors de la publication des équipes :", error);
  }
  try {
    for(let i = 0; i < formattedTeamsData.length; i++){
      if(etatEquipes === "PREPUBLIE")
        await setRole(formattedTeamsData[i].staff.id, 3);   //Supervising Staff
      for(let y = 0; y < formattedTeamsData[i].members.length; y++){
        await setRole(formattedTeamsData[i].members[y].id, 5);  //Team Member
      }
    }
  } catch (error) {
    console.error("Erreur lors de l'attribution des rôles :", error);
  }
};

const modifyTeams = async() => {
  try{
    await modifierEquipes('GENERE');
  } catch(error){
    console.error(error)
  }
  try {
    await deleteValidationEquipes();
  } catch (error) {
    console.error(error);
  }
  nbValidation.value = 0;
  etatEquipes.value = 'GENERE';
  canModifyTeams.value = true;
  equipesData.value = await fetchEquipes();
}


// Fonction permettant de changer la donnée recevant les modifications dues à l'évènement
async function handleCustomEvent() {
  const etudiants = await fetchEtudiants() || [];
  const teamsMap = {};
  for (const etudiant of etudiants) {
    const moyenneEtudiant = await fetchMoyenne(etudiant);
    const equipe = etudiant.equipe;
    if (!teamsMap[equipe.id]) {
      teamsMap[equipe.id] = {
        name: equipe.nom,
        staff: equipe.utilisateur,
        etat: equipe.etatEquipes,
        members: []
      };
      etatEquipes.value = teamsMap[equipe.id].etat;
    }
    teamsMap[equipe.id].members.push({
      id: etudiant.id,
      nom: etudiant.nom,
      prenom: etudiant.prenom,
      genre: etudiant.genre,
      typeUtilisateur: etudiant.typeUtilisateur,
      moyenne: moyenneEtudiant
    });
  }
  teamsData.value = Object.values(teamsMap);
}
</script>

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

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }

  100% {
    transform: rotate(360deg);
  }
}

.loader {
  border: 8px solid #f3f3f3;
  border-top: 8px solid #3498db;
  border-radius: 50%;
  width: 50px;
  height: 50px;
  animation: spin 1s linear infinite;
}
</style>