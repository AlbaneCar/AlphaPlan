<template>
    <Suspense>
        <div class="flex-1 p-6 flex flex-col bg-custom-sideBarBG">
            <Title class="mb-4">Questions / Réponses</Title>
            <ListConversations :key="updateKey" @message-sent-up="handleMessageSent" :conversations="allConversations"
                :selectedConversation="selectedConversation" />
            <div v-if="(currentUserRole != 'TEAM_MEMBER')">
                <Button id="NouvelleQuestion" icon='plus' class="py-2 px-4 mt-2" @click="openCreateForm">Nouvelle question</Button>
            </div>
        </div>
        <ConversationForm :visibilite="isFormVisible" @update:visibilite="() => changeVisibility()" />
    </Suspense>
</template>
<script setup>
import { ref } from 'vue';
import { getConversations, getMessages, getConversationsTeam } from '../services/ConversationsService';
import Button from '../components/Buttons/Button.vue';
import Title from '../components/Title.vue';
import ListConversations from '../components/ListConversations.vue';
import ConversationForm from '../components/ConversationForm.vue';

const ROLES = {
    PROJECT_LEADER: 'PROJECT_LEADER',
    OPTION_LEADER: 'OPTION_LEADER',
    SUPERVISING_STAFF: 'SUPERVISING_STAFF',
    TEAM_MEMBER: 'TEAM_MEMBER',
    TECHNICAL_COACHES: 'TECHNICAL_COACHES',
    OPTION_STUDENT: 'OPTION_STUDENT',
};

const nom = localStorage.getItem('nom');
const prenom = localStorage.getItem('prenom');
const currentUserRole = ROLES[JSON.parse(localStorage.getItem('roles'))];
const teamId = localStorage.getItem('teamId');
const etudiantId = localStorage.getItem('user_id');

const allConversations = ref([]);
const selectedConversation = ref(null);

const updateKey = ref(0);

const isFormVisible = ref(false);

function changeVisibility() {
    isFormVisible.value = !isFormVisible.value;
}

const openCreateForm = () => {
    changeVisibility();
}

const loadData = async () => {
    try {
        if (currentUserRole == 'TEAM_MEMBER') {
            const conversations = await getConversationsTeam(teamId);
            /*   for (const conv of conversations) {
                   conv.messages = await getMessages(conv.id);
               }*/
            allConversations.value = conversations;
        } else {
            const conversations = await getConversations();
            /*   for (const conv of conversations) {
                   conv.messages = await getMessages(conv.id);
               }*/
            allConversations.value = conversations;
        }
        updateKey.value++;
    } catch (error) {
        console.error('Error fetching data:', error);
    }
};

const handleMessageSent = (conversation) => {
    selectedConversation.value = conversation;
    loadData();
};

loadData();
</script>