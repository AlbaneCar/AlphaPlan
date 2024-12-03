<script setup>

import { ref } from 'vue';
import { getAllSprints } from '../../services/SprintService';
import { Check } from 'lucide-vue-next';

const props = defineProps({
    sprints: Array,
    actualSprint : Object,
});

const numActiveSprint = ref(props.actualSprint != null ? props.sprints.findIndex(sprint => sprint.id === props.actualSprint.id)+ 1 : 0);


function getStatus(indexSprint) {
    // Retourner 0 si le sprint n'existe pas :
    if (indexSprint < 1 || indexSprint > props.sprints.length) {
        return 0;
    }

    const sprint = props.sprints[indexSprint - 1];
    
    // Date du Jour sans l'heure :
    const today = new Date();
    const todayWithoutTime = new Date(today.getFullYear(), today.getMonth(), today.getDate());
    const startDate = new Date(sprint.startDate);
    const startDateWithoutTime = new Date(startDate.getFullYear(), startDate.getMonth(), startDate.getDate());

    const endDate = new Date(sprint.endDate);
    const endDateWithoutTime = new Date(endDate.getFullYear(), endDate.getMonth(), endDate.getDate());

    //Vérifier si le sprint est en cours
    if (todayWithoutTime >= startDateWithoutTime && todayWithoutTime <= endDateWithoutTime) {
        return 1; // Sprint en cours
    } else if (todayWithoutTime < startDateWithoutTime) {
        return 0; // Sprint à venir
    } else {
        return 2; // Sprint terminé
    }
}

function formatDate(date) {
    const dateObject = new Date(date);
    const day = dateObject.getDate() < 10 ? '0' + dateObject.getDate() : dateObject.getDate();
    const month = dateObject.getMonth() + 1 < 10 ? '0' + (dateObject.getMonth() + 1) : dateObject.getMonth() + 1;
    return `${day}/${month}`;
}

</script>

<template>
    <div class="flex justify-center items-center w-full">
        <template v-for="x in props.sprints.length">
            <div v-if="x != numActiveSprint && getStatus(x) == 0" class="flex flex-col items-center">
                <div
                    class="size-8 rounded-full bg-white border-2 border-gray-300 font-CASlalomBold mb-1 flex justify-center items-end text-gray-600">
                    {{ x }}
                </div>
                <div class="text-gray-700 text-sm font-CASlalomBold">Sprint n° {{ x }}</div>
                <div class="text-xs">{{ formatDate(props.sprints[x - 1].startDate) }} -> {{ formatDate(props.sprints[x - 1].endDate) }}</div>
            </div>
            <div v-else class="flex flex-col items-center">
                <div v-if="getStatus(x) == 2"
                    class="size-8 rounded-full bg-white border-2 border-custom-lightblueAP font-CASlalomBold mb-1 flex justify-center items-center text-custom-lightblueAP">
                    <Check />
                </div>
                <div v-else
                    class="size-8 rounded-full bg-custom-lightblueAP border-2 border-custom-lightblueAP font-CASlalomBold mb-1 flex justify-center text-white animate-pulse items-end">
                    {{ x }}
                </div>
                <div class="text-custom-lightblueAP text-sm font-CASlalomBold">Sprint n° {{ x }}</div>
                <div class="text-xs text-custom-lightblueAP">{{ formatDate(props.sprints[x - 1].startDate) }} -> {{ formatDate(props.sprints[x - 1].endDate) }}</div>
            </div>
            
            <div v-if="x != props.sprints.length && getStatus(x) != 2" class="flex-1 flex px-2 max-w-64">
                <div class="flex-1 border-[1.5px] border-gray-300"></div>  
            </div>
            <div v-else-if="x != props.sprints.length && getStatus(x) == 2 && getStatus(x + 1) != 0" class="flex-1 flex px-2 max-w-64">
                <div class="flex-1 border-[1.5px] border-custom-lightblueAP"></div>  
            </div>
            <div v-else-if="x != props.sprints.length" class="flex-1 flex px-2 max-w-64">
                <div class="flex-1 border-[1.5px] border-custom-lightblueAP"></div>
                <div class="flex-1 border-[1.5px] border-gray-300"></div>               
            </div>
        </template>
    </div>
</template>