<template>
    <div class="relative w-48">
        <div class="w-full overflow-hidden overflow-ellipsis whitespace-nowrap rounded-md p-2 bg-[#332466] text-white cursor-pointer hover:bg-custom-sideBarActiveBG" @click="show = !show && conversations.length"
        v-tooltip.top="{value: 'Participants: ' + participants, disabled: !(conversations.length > 0), pt: {text: 'text-xs'}}">
            {{ conversations.length > 0 ? selectedConversation.titre : 'Aucune conversation' }}
        </div>
        <ul v-show="show" class="absolute w-full mt-2 bg-white text-black rounded-md border shadow-md">
            <li v-for="conversation in conversations" :key="conversation.id" @click="selectConversation(conversation)" class="px-1 py-2 hover:bg-gray-200 cursor-pointer">
                <div class="flex flex-row overflow-hidden overflow-ellipsis whitespace-nowrap">
                    <p class="flex flex-col">
                        {{ conversation.titre }}
                        <br>
                        <span class="text-xs">{{ '/' + getParticipants() }}</span>
                    </p>
                </div>
            </li>
        </ul>
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted, watchEffect } from 'vue';

const props = defineProps({
    conversations: Array
});

// Variables
const show = ref(false);
let userRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));

// On défini la conversation sélectionnée
const selectedConversation = ref(null);
const participants = ref('');

// Define the emits function
const emit = defineEmits(['update:selectedConversation']);

// Fonction pour sélectionner une conversation
const selectConversation = (conversation) => {
    // Si la conversation sélectionnée est la même que celle actuellement sélectionnée, on ne fait rien
    if (selectedConversation.value === conversation) return;
    // Sinon, on met à jour la conversation sélectionnée
    selectedConversation.value = conversation;
    participants.value = getParticipants();
    show.value = false;
    emit('update:selectedConversation', selectedConversation.value);
}

// Fonction pour récupérer les participants de la conversation
const getParticipants = () => {
    let participants = '';
    selectedConversation.value.participants.forEach(participant => {
        participants += participant.nom + ' ' + participant.prenom + ', ';
    });
    return participants.slice(0, -2);
}

// Fonction pour construire une chaîne de caractères des rôles de l'utilisateur
function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}

// Appel de la fonction selectConversation() au montage du composant
onMounted(() => {
    if (props.conversations.length > 0) {
        selectConversation(props.conversations[0]);
        participants.value = getParticipants();
    }
});

// On regarde si la conversation sélectionnée est dans la liste des conversations
watchEffect(() => {
    if (props.conversations.length > 0 && !props.conversations.includes(selectedConversation.value)) {
        selectConversation(props.conversations[0]);
        participants.value = getParticipants();
    }
});
</script>