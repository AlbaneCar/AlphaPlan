  <template>
      <div v-if="equipes && equipes.length != 0" class="grid grid-cols-2 gap-4 justify-center p-4 mb-4 mt-6 m-6">
        <div v-for="(equipe, index) in equipes" :key="index">
          <table class="w-full text-sm text-left rtl:text-right text-gray-500 rounded-lg overflow-hidden">
            <thead class="text-xs text-gray-700 uppercase bg-gray-100 font-CASlalomBold border">
              <tr>
                <th class="px-6 py-3 text-center"   >{{ equipe.nom }}</th>
                <th class="px-6 py-3 text-center"  >{{ equipe.utilisateur?.nom }} {{ equipe.utilisateur?.prenom }}</th>
                <th class="text-right" ><Button class="mr-2 px-2" @click="openSignalementForm(equipe)">Signalement</Button> </th>
              </tr>
            </thead>
            <tbody class="border-l border-r">
              <tr v-for="membre in equipe.membres" :key="membre.id" class="bg-white border-b hover:bg-gray-50">
                <td class="text-center">
                  <a v-if="membre.genre == 'HOMME'" class="bg-custom-lightblueAP rounded-md text-white text-center w-fit px-2" >H</a>
                  <a v-else class="bg-pink-400 rounded-md text-white text-center w-fit px-2" >F</a>
                  <a v-if="membre.typeUtilisateur == 'BACHELOR'" class="bg-purple-400 rounded-md text-white text-center w-fit ml-1 px-2" >B</a>
                </td>
                <td class="px-6 py-2 text-center uppercase " >{{ membre.nom }}</td>
                <td class="px-6 py-2 text-center" >{{ membre.prenom }}</td>
              </tr>
            </tbody>
            <thead class="text-xs text-gray-700 uppercase bg-gray-100 font-CASlalomBold border">
              <tr>
                <th class="py-2 text-center">H/F: {{ countGender(equipe.membres, 'HOMME') }}/{{ countGender(equipe.membres, 'FEMME') }}</th>
                <th class="py-2 text-center">E3E/B: {{ countUserType(equipe.membres, 'E3E') }}/{{ countUserType(equipe.membres, 'BACHELOR')}}</th>
                <th class="py-2 text-center">Moyenne:  {{ equipe.moyenne }}</th>
              </tr>
            </thead>

            

          </table>
          <!-- <button @click="toggleCommentInput"
            class="bg-custom-lightblueAP rounded-md text-white border-none px-2 py-1 mb-2 font-CASlalomBold
                    hover:bg-custom-darkblueAP hover:text-white duration-300 hover:transition-colors px-5 py-2.5 text-center mr-2 mb-2 mt-3">Signaler
            un problème</button>
          <div v-if="showCommentInput" class="mt-4">
            <textarea v-model="commentaire" class="w-full border rounded-md p-2 text-center text-sm"
              placeholder="Signalez votre problème avec d'éventuelles solutions"></textarea>
            <button @click="signalerProbleme(equipe.id_Equipe)" class="bg-custom-lightblueAP rounded-md text-white
              border-none px-2 py-1 mb-2 font-CASlalomBold hover:bg-custom-darkblueAP hover:text-white duration-300
              hover:transition-colors font-medium rounded-lg text-sm px-5 py-2.5 text-center me-2 mb-2">➤</button>
          </div> -->
        </div>
      </div>
    <div v-else-if="equipes && equipes.length == 0" class="flex flex-1 justify-center items-center">
      <div class="flex justify-center items-center gap-6">
        <Illustration nameImage="nothing2.svg" :width="200" />
        <Title>Il n'y a pas d'équipes enregistrées</Title>
      </div>
    </div>

    <SignalementForm :equipe="selectedEquipe" :visibilite="isSignalementFormVisible"
    @update:visibilite="() => changeVisibility()" id="dialogSignalementEquipe"/>
    
    <div class="flex">
      <Dialog v-model:visible="visible" modal header="Refuser les équipes" class="min-w-[500px]">
        <p class="mb-2">Merci de renseigner la raison de votre refus</p>
        <input type="text" v-model="inputRaison" placeholder="Veuillez entrer la raison de votre refus" class="w-full border rounded-md border-gray-400 p-1">
        <div class="text-center mt-4">
          <Button @click="deleteValidation()" classes="p-2 mr-8">Refuser</Button>
          <Button @click="visible = !visible" classes="p-2">Annuler</Button>
        </div>
      </Dialog>
      <Button v-if="isSS && !hasValidated && etatEquipes === 'PREPUBLIE'" @click="postValidation()" class="w-32 m-auto p-1">Valider les équipes</Button>
      <Button v-if="isSS && !hasValidated && etatEquipes === 'PREPUBLIE'" @click="visible = true" class="w-32 m-auto p-1 bg-red-500 text-white">Refuser les équipes</Button>
    </div>

  </template>

<script setup>
import { ref, onMounted } from 'vue';
import { fetchEquipes, fetchOLPL } from '../services/EquipesService';
import { fetchEtudiants, fetchRoles, fetchMoyenne } from '../services/EtudiantService';
import { useToast } from "vue-toastification";
import Button from './Buttons/Button.vue';
import Illustration from './NoDataIllustration/Illustration.vue';
import Title from './Title.vue';
import SignalementForm from './SignalementForm.vue';
import { getValidationEquipes, postValidationEquipes, deleteValidationEquipes } from '../services/CreerEquipesService';
import { submitFormData } from '../services/PresentationFormService';
import Dialog from 'primevue/dialog';

const equipes = ref([]);
const showCommentInput = ref(false);
const commentaire = ref([]);
const toast = useToast();

const isSignalementFormVisible = ref(false);
const selectedEquipe = ref(null);

const isSS = ref(false);
const hasValidated = ref(false);
const userId = ref(localStorage.getItem('user_id'))
userId.value = parseInt(userId.value, 10);

const etatEquipes = ref();
const visible = ref(false);
const inputRaison = ref('');

//au lancement du composant
onMounted(async () => {
  const roles = JSON.parse(localStorage.getItem('roles'));
  roles.forEach(role => {
    if (role === "SUPERVISING_STAFF") {
      isSS.value = true;
      return;
    }
  });

  const validations = await getValidationEquipes();
    validations.forEach((item) => {
        if(item.utilisateur.id === userId.value) {
            hasValidated.value = true;
            return;
        }
    });

  try {
    equipes.value = await fetchEquipes() || [];
    const etudiants = await fetchEtudiants();

    const etudiantsAvecEquipe = etudiants ? etudiants.filter(etudiant => etudiant.equipe && etudiant.equipe.id) : [];


    for (const etudiant of etudiantsAvecEquipe) {
      const roleDetails = await fetchRoles(etudiant.id);
      const equipe = equipes.value.find(e => e.id === etudiant.equipe.id);

      if (equipe) {
        equipe.membres = equipe.membres || [];
        equipe.membres.push({
          ...etudiant,
          roles: roleDetails
          // role: "malice"
        });
      }
    }

   if(equipes.value && equipes.value.length !== 0) {
      await Promise.all(equipes.value.map(async equipe => {
        equipe.moyenne = await fetchMoyenne(equipe.id);
      }));
    }

    etatEquipes.value = equipes.value[0].etatEquipes;

  } catch (error) {
    console.table(error);
    console.error('Erreur lors de la récupération des équipes, des étudiants ou des détails du rôle:', error);
  }
});


//signalement d'un problème (ne s'affiche que pour l'équipe de l'utilisateur connecté)
const signalerProbleme = async (idEquipe) => {
  const url = import.meta.env.VITE_IP_BACKEND + `notification/ajouter`;
  const datas = {
    message: commentaire.value,
    type: "RECLAMATION"
  };

  try {
    const response = await fetch(url, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer ' + getCookieToken()
      },
      body: JSON.stringify(datas)
    });

    if (response.ok) {
      toast.success("Notification envoyée", {position : "top-center"});
      showCommentInput.value = !showCommentInput.value;
    } else {
      toast.error("Une erreur s'est produite lors de l'envoi de la notification.", {position : "top-center"});
    }
  } catch (error) {
    toast.error("Erreur lors de l'envoi de la notification:", {position : "top-center"});
  }
};

//définition du nombre d'H/F
const countGender = (membres, gender) => {
  return membres.filter(membre => membre.genre === gender).length;
};

const countUserType = (membres, userType) => {
  return membres.filter(membre => membre.typeUtilisateur === userType).length;
};

function changeVisibility() {
  isSignalementFormVisible.value = !isSignalementFormVisible.value;
}

const openSignalementForm = (equipe) => {
  selectedEquipe.value = equipe;
  changeVisibility();
}

const postValidation = async() => {
  const OLPL = await fetchOLPL();
  for(let i = 0; i < OLPL.length; i++){
    const OLPLId = OLPL[i].id;
    const message = "Je viens de valider la composition des équipes proposée";
    const formData = {
      message,
      type: 'EQUIPES',
      envoyeur: {"id" : localStorage.getItem('user_id') },
      receveur: {"id" : OLPLId}
    };
    try {
      await submitFormData(formData);
    }catch(error){
      console.error(error);
    }
  }
  await postValidationEquipes(userId.value);
  hasValidated.value = true;
}

const deleteValidation = async() => {
  if(inputRaison.value){
    console.log(inputRaison.value);
    const OLPL = await fetchOLPL();
    for(let i = 0; i < OLPL.length; i++){
      const OLPLId = OLPL[i].id;
      const message = inputRaison.value;
      const formData = {
        message,
        type: 'EQUIPES',
        envoyeur: {"id" : localStorage.getItem('user_id') },
        receveur: {"id" : OLPLId}
      };
      try {
        await submitFormData(formData);
      }catch(error){
        console.error(error);
      }
    }
    for(let i = 0; i < equipes.value.length; i++){
      const staffId = equipes.value[i].utilisateur.id;
      const message = inputRaison.value;
      const formData = {
        message,
        type: 'EQUIPES',
        envoyeur: {"id" : localStorage.getItem('user_id') },
        receveur: {"id" : staffId}
      };
      try {
        await submitFormData(formData);
      }catch(error){
        console.error(error);
      }
    }
    try{
      await deleteValidationEquipes();
    }catch(error){
    }
    toast.success('Les équipes ont bien été refusé !', {position : 'top-center'});

    etatEquipes.value = 'GENERE';
    visible.value = false;
  }
  else{
    toast.error('Veuillez renseigner une raison !', {position: 'top-center'})
  }  
};

</script>

<style scoped></style>
