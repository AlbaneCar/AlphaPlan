<script setup>

import Illustration from '../NoDataIllustration/Illustration.vue';
import Notification from '../Notification.vue';

const props = defineProps({
    notifications: Array,
});

const emits = defineEmits(['deleteNotification']);

</script>

<template>
    <div v-if="props.notifications.length != 0" class="flex flex-col w-full gap-3 py-3 px-0 overflow-y-scroll h-full">
        <Notification v-for="notification in notifications" :notification="notification" 
            @deleteNotification="(notif) => $emit('deleteNotification', notif)" :key="notification.id"
        />
    </div>
    <div v-else class="flex flex-col w-full h-full justify-center items-center gap-3">
        <Illustration nameImage="notif.svg" :width="180" />
        <div class="text-xl text-custom-darkblueAP font-CASlalomBold">Aucune notification !</div>
    </div>
</template>