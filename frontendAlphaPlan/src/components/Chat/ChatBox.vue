<script setup>
import { ref, defineProps, watch } from 'vue';
import { getMessages, sendMessage } from '../../services/ConversationsService.js';
import { createNotification } from '../../services/NotificationService.js';
import { useToast } from 'vue-toastification';

const props = defineProps({
    conversation: Object
});

// Variables
const toast = useToast();
const messages = ref([]);
const conversation = ref(null);
const userId = JSON.parse(localStorage.getItem('user_id'));

// Variable pour le nouveau message
const newMessage = ref('');

// On charge la conversation
conversation.value = props.conversation;

// Fonction pour charger les messages
const loadMessages = async () => {
    // Si la conversation n'est pas définie, on ne fait rien
    if (conversation.value == null) return;
    // Sinon, on récupère les messages de la conversation
    const response = await getMessages(conversation.value.id); 
    messages.value = response;
    // On inverse la liste (pour auto scroll down)
    messages.value.reverse();
}

// Fonction pour envoyer un message
const envoyerMessage = async () => {
    // On duplique le message pour savoir s'il est vide
    let temp = newMessage.value;
    // Si le message est vide, on envoit rien
    if (temp.replace(" ", "") == '') return;
    // On envoie le message
    const response = await sendMessage(conversation.value.id, newMessage.value, userId);
    // On envoie une notification
    if (response) {
        let envoyeur = conversation.value.participants.filter(participant => participant.id == userId);
        let message = 'Nouveau message de la part de ' + envoyeur[0].prenom + ' ' + envoyeur[0].nom + ' dans la conversation ' + conversation.value.titre;
        conversation.value.participants.forEach(async participant => {
            if (participant.id != userId) {
                const response2 = await createNotification('NOUVEAU_MESSAGE', message, userId, participant.id);
                if (!response2) {
                    toast.error('Erreur lors de l\'envoi de la notification');
                }
            }
        });
    } else {
        toast.error('Erreur lors de l\'envoi du message');
    }
    // On recharge les messages
    loadMessages();
    newMessage.value = '';
}

// On surveille la conversation et on charge les messages chaque fois qu'elle change
watch(() => props.conversation, (newConversation) => {
    conversation.value = newConversation;
    if (conversation.value == null) return;
    loadMessages();
}, { immediate: true });
</script>

<template>
    <div class="w-full max-h-96 h-fit flex flex-col gap-2 justify-end">

        <!-- S'il n'y a pas de conversation -->
        <p v-if="conversation == null" class="italic text-gray-200 text-sm p-2">Il n'y a aucune conversation.</p>

        <!-- S'il n'y a pas de messages dans la conversation -->
        <p v-else-if="!messages.length" class="italic text-gray-200 text-sm p-2">C'est bien silencieux ici !</p>

        <!-- S'il y a des messages dans la conversation, on les affiche -->
        <div v-else class="w-full h-full overflow-y-auto mt-3 flex flex-col-reverse">
            <div v-for="(message, index) in messages" :key="message.id" class="flex flex-col w-full"
            :class="{'items-start': userId != message.auteur.id, 'items-end': userId == message.auteur.id}">

                <!-- Contenu du message -->
                <p class="flex justify-start items-center h-fit w-40 rounded-md text-white text-sm p-2 break-all"
                :class="{'bg-[#3B3257]': userId != message.auteur.id, 'bg-[#5427ED]': userId == message.auteur.id, 'mb-1 rounded-b-none': (index > 0) && (messages[index - 1].auteur.id === message.auteur.id), 'rounded-t-none': (index < messages.length - 1) && (messages[index + 1] && messages[index + 1].auteur.id === message.auteur.id)}">
                    {{ message.message }}
                </p>
                <p v-if="(index === 0) || (messages[index - 1].auteur.id !== message.auteur.id)" class="text-xs flex text-gray-400 mt-1"
                :class="{'pl-1': userId != message.auteur.id, 'pr-1': userId == message.auteur.id, 'mb-3': index === messages.length - 1}">
                    {{ (userId != message.auteur.id) ? message.auteur.prenom + ' ' + message.auteur.nom : 'Moi'}}
                </p>
            </div>
        </div>

        <!-- Champ pour envoyer un message -->
        <div v-if="conversation != null" class="flex flex-row justify-between items-center w-full">
            <input type="text" v-model="newMessage" @keyup.enter="envoyerMessage" class="p-2 bg-custom-sideBarActiveBG text-white rounded-md text-sm w-full" placeholder="Aa"/>
        </div>
    </div> 
</template>