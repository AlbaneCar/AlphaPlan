<template>
    <div class="card flex justify-content-center">
        <Dialog v-model:visible="visible" modal :header="'Poser une nouvelle question'" class="min-w-[550px] min-h-[300px] border-4">
            <div class="flex flex-col justify-start items-center p-2 gap-3">
                <input type="text" v-model="titre"
                    class="w-[500px] h-[50px] w-full px-4 py-2 mt-2 font-CASlalomBold italic text-xl font-normal border border-gray-300 rounded-lg h-20 resize-none"
                    placeholder="Titre de la conversation"
                    >

                <select v-model="selectedTeam" class="w-[500px] h-[50px] w-full px-4 py-2 mt-2 font-CASlalomBold italic text-xl font-normal border border-gray-300 rounded-lg h-20 resize-none">
                    <option v-for="team in teams" :key="team.id" :value="team.id">{{ team.nom }}</option>
                </select>

                <textarea v-model="question"
                    class="w-[500px] h-[100px] w-full px-4 py-2 mt-2 font-CASlalomBold italic text-xl font-normal border border-gray-300 rounded-lg h-20 resize-none"
                    placeholder="Votre question"
                    >
                </textarea>

                <Button v-if="!isLoadding" class="mt-4 py-2 px-4" @click="createQuestion">Créer la conversation</Button>
                <LoadingButton v-if="isLoadding" icon="save" class="mt-4 py-2 px-4" @click="createQuestion">Créer la conversation</LoadingButton>

                <p v-if="isError" class="font-CASlalomBold font-normal text-red-400">{{ erreurMessage }}</p>
                <p v-if="!isError" class="font-CASlalomBold font-normal text-green-400">{{ okMessage }}</p>
            </div>
        </Dialog>
    </div>
</template>

<script setup>
import Dialog from 'primevue/dialog';
import { ref, defineProps, watch, onMounted } from 'vue';
import Button from '../components/Buttons/Button.vue';
import LoadingButton from '../components/Buttons/LoadingButton.vue';
import { createConv, sendNotifCreation } from '../services/ConversationsService';
import { fetchEquipes } from '../services/EquipesService';
import { useToast } from 'vue-toastification';
import { getTeamInformations, getTeamName } from '../services/BonusMalusTMService';

const toast = useToast();

const props = defineProps({
    visibilite: {
        type: Boolean,
    }
});

const titre = ref('');
const question = ref('');
const erreurMessage = ref('');
const okMessage = ref('');
const visible = ref(false);
const isLoadding = ref(false);
const isError = ref(false);

const selectedTeam = ref(null);
const teams = ref([]);

const teamMembers = ref([]);
const equipeDetails = ref([]);

const etudiantId = localStorage.getItem('user_id');

watch(() => props.visibilite, (value) => {
    visible.value = value;
});

const createQuestion = async () => {
    try {
        isLoadding.value = true;
        const success = await createConv(titre.value, selectedTeam.value, question.value, localStorage.getItem('user_id'));
        await loadInfos();
        const success2 = await sendNotifCreation(teamMembers.value, etudiantId);
        if (success && success2){
            toast.success("Conversation créée !", {
                position: "top-center",
            });
            setTimeout(() => {
                location.reload();
            }, 2500);
        }
    } catch (error) {
        isError.value = true;
        erreurMessage.value = error.message || 'Une erreur inconnue s\'est produite.';
/*        toast.error(erreurMessage.value, {
            position: "top-center",
        });
*/    } finally {
        isLoadding.value = false;
    }
};

const loadTeams = async () => {
  teams.value = await fetchEquipes();
};


const loadInfos = async () => {
    try {
        console.log(selectedTeam.value);
        const resp = await getTeamName(selectedTeam.value);
        equipeDetails.value = resp;

        const response = await getTeamInformations(selectedTeam.value);
        const listNotif = await Promise.all(response.map(async (member) => {
          return member.id;
        }));

        teamMembers.value = listNotif;
    } catch (error){
    }
}

loadTeams();

</script>


<style scoped>
</style>