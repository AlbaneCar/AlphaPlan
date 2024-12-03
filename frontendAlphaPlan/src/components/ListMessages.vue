<template>
<div class="flex flex-col bg-white border border-gray-400 shadow-sm h-[600px] overflow-y-auto rounded-md gap-2 w-3/4">
    <div v-if="conversation">
        <div class="flex items-center justify-between">
            <div class="text-2xl text-black font-bold font-CASlalomBold mb-3 p-2">{{ conversation.titre }}</div>
            <SuppConv v-if="conversation.auteur.id == etudiantId" class="p-2 m-2" :id="conversation.id" />
        </div>
        <div class="h-[450px] overflow-y-auto">
            <div v-if="conversation.messages && conversation.messages.length">
                <div v-for="message in conversation.messages" :key="message.id" class="mb-1 ml-2">
                    <p>
                        <span class="bg-blue-100 p-2 rounded-lg inline-block">
                            <span class="text-lg font-medium font-CASlalomRegular">{{ message.message }}</span>
                        </span>
                    </p>
                    <p class="text-neutral-400 text-sm font-normal font-Roboto">
                        <em>{{ message.auteur.prenom }} {{ message.auteur.nom }}</em>
                    </p>
                </div>
            </div>
            <div v-else>
                <p class="ml-2">Aucun message pour le moment</p>
            </div>
        </div>
    </div>
    <div v-else class="flex items-center justify-center h-full">
        <p class="p-2 text-lg font-medium font-CASlalomRegular text-center">Sélectionnez une conversation pour afficher les messages.</p>
    </div>
    <div v-if="conversation" class="mt-auto flex justify-between items-center m-4">
        <textarea v-model="newMessage" class="border border-gray-300 rounded-xl p-2 w-full mx-4" placeholder="Répondre ..."></textarea>
        <Button icon='send' class="p-2" @click="send"></Button>
    </div>
</div>
</template>


<script setup>
import { defineProps, ref, defineEmits, onMounted } from 'vue';
import Button from '../components/Buttons/Button.vue';
import { sendMessage, getMessages, sendNotif } from '../services/ConversationsService.js';
import { useToast } from 'vue-toastification';
import SuppConv from '../components/PopUpBoutonSupConv.vue';
import { getTeamInformations, getTeamName } from '../services/BonusMalusTMService';

const toast = useToast();

const props = defineProps({
    conversation: Object
});

const etudiantId = localStorage.getItem('user_id');

const newMessage = ref('');
const emit = defineEmits(['message-sent']);

const teamMembers = ref([]);
const equipeDetails = ref([]);

const send = async () => {
    await loadInfos();
    const trimmedMessage = newMessage.value.trim();
    if(trimmedMessage.length > 0){
        const success = await sendMessage(props.conversation.id, newMessage.value, etudiantId);
        const success2 = await sendNotif(teamMembers.value, etudiantId);
        if (success && success2) {
            toast.success("Message envoyé !", {
                position: "top-center",
            });
            newMessage.value = '';
        } else {
            toast.error("Erreur lors de l'envoi du message", {
                position: "top-center",
            });
        }
    }else{
        toast.error("Veuillez rentrer un message valide", {
            position: "top-center",
        });
    }
};

const loadData = async () => {
    try {
        props.conversation.messages = await getMessages(props.conversation.id);
    } catch (error) {
    }
};

const loadInfos = async () => {
    try {
        const resp = await getTeamName(props.conversation.equipe.id);
        equipeDetails.value = resp;

        const response = await getTeamInformations(props.conversation.equipe.id);
        const listNotif = await Promise.all(response.map(async (member) => {
          return member.id;
        }));

        const auteurId = props.conversation.auteur.id;
        listNotif.push(auteurId);

        const filteredTeamMembers = listNotif.filter(id => {
            return id != etudiantId;
        });

        teamMembers.value = filteredTeamMembers;
    } catch (error){
    }
}

onMounted(() => {
    loadData();
    const timer = setInterval(loadData, 500);
    onMounted(() => {
        clearInterval(timer);
    });
});

</script>
