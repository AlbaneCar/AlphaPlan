<script setup>

import { defineProps, ref } from 'vue';

const props = defineProps({
    actualSprint: Object,
});

const day = ref(null);
const month = ref(null);
const year = ref(null);
const daysRemaining = ref(null);

if(props.actualSprint) {

    const endDate = new Date(props.actualSprint.endDate);
    const today = new Date();
    day.value = endDate.getDate();
    month.value = endDate.getMonth() + 1;
    year.value = endDate.getFullYear();
    // Si mois ou jour < 10, ajouter un 0 devant
    if(day.value < 10) day.value = '0' + day.value;
    if(month.value < 10) month.value = '0' + month.value;

    const timeDiff = endDate.getTime() - today.getTime();
    daysRemaining.value = Math.ceil(timeDiff / (1000 * 3600 * 24));
}
</script>

<template>
    <p v-if="!actualSprint" class="flex justify-center items-center font-Roboto text-custom-darkblueAP text-sm w-full h-full">
        Rien à voir pour le moment !
    </p>
    <div v-else class="bg-custom-sideBarBG rounded-md border border-gray-200 shadow-sm  w-full p-2 flex justify-between items-center">
        <template v-if="actualSprint.sprintEndType == 'PRESENTATION'">
            <div class="bg-custom-lightblueAP text-white px-2 rounded-sm font-CASlalomBold text-sm">Présentation</div>
            <div class="text-sm font-bold text-custom-darkblueAP">{{ day }}/{{ month }}/{{ year }}</div>
        </template>
        <template v-else>
            <div class="bg-purple-800 text-white px-2 rounded-sm font-CASlalomBold text-sm">Revue de sprint</div>
            <div class="text-sm font-bold text-custom-darkblueAP">{{ day }}/{{ month }}/{{ year }}</div>
        </template>
    </div>
</template>