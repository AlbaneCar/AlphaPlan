<template>
    <div class="flex flex-row flex-1">
        <div class="flex flex-col bg-white border border-gray-400 shadow-sm h-[600px] overflow-y-auto rounded-md gap-2 w-1/4">
            <div class="text-2xl text-black font-bold font-CASlalomBold p-2">Conversations</div>
            <div v-if="conversations.length">
                <div v-for="conversation in conversations" :key="conversation.id" class="mb-2 relative cursor-pointer" :class="{ 'bg-blue-100': selectedConversation === conversation }" @click="selectConversation(conversation)" :data-conversation-id="conversation.id" >
                <div v-if="selectedConversation === conversation" class="absolute top-0 left-0 h-full bg-blue-600 w-1"></div>
                <p class="text-lg font-medium font-CASlalomBold mb-0 ml-2 overflow-hidden whitespace-nowrap">{{ conversation.titre }}</p>
                <p class="text-neutral-400 text-sm font-normal font-Roboto ml-2"><em>De {{ conversation.auteur.prenom }} {{ conversation.auteur.nom }} pour {{ conversation.equipe.nom }}</em></p>
            </div>
            </div>
            <div v-else>
                <p class="ml-2">Aucune conversation</p>
            </div>
        </div>
        <ListMessages :key="updateKey" @message-sent="forwardMessageSent" :conversation="selectedConversation" />
    </div>
</template>

<script setup>
import { ref, defineProps, defineEmits, onMounted } from 'vue';
import ListMessages from './ListMessages.vue';

const emit = defineEmits(['message-sent-up']);

const forwardMessageSent = () => {
    updateKey.value++;

    emit('message-sent-up', selectedConversation);
};

const props = defineProps({
  conversations: Array,
  selectedConversation: Object
});

const updateKey = ref(0);

const selectedConversation = ref(null);

const selectConversation = (conversation) => {

  selectedConversation.value = conversation;
};

onMounted(() => {
  if (props.selectedConversation !== null) {
    selectConversation(props.selectedConversation.value);
  }
});

const loadData = async () => {
    try {
        if(props.selectedConversation.value !== null){
            selectConversation(props.selectedConversation.value);
        } else {
        }
    } catch (error) {
    }
};

loadData();

</script>
