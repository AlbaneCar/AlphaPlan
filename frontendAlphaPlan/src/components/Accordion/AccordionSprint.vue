<template>

    <Accordion :multiple="true" :activeIndex="0" class="bg-white flex flex-col gap-2 rounded-md border shadow-sm border-gray-200">
        <AccordionTab :header="currentSprint.name">
            <div class="flex flex-wrap justify-between items-center">
                <div>
                    <span class="font-bold">Date de début : </span>
                    <span>{{ formatDate(currentSprint.startDate) }}</span>
                </div>
                <div>
                    <span class="font-bold">Date de fin : </span>
                    <span>{{ formatDate(currentSprint.endDate) }}</span>
                </div>
                <div>
                    <span class="font-bold">Type de fin de sprint : </span>
                    <span>{{ formatSprintEndType(currentSprint.sprintEndType) }}</span>
                </div>
                <Pencil @click="openDialog"
                    class="bg-custom-lightblueAP text-white h-fit w-fit p-1 rounded-md cursor-pointer hover:bg-custom-darkblueAP duration-200 ease-in-out" />
            </div>
        </AccordionTab>
    </Accordion>

    <DialogCalcul :visibilite="visibleModifDialog" :sprints="sprints" :index="index"
        @update:visibilite="value => { if (value && value.value !== undefined) visibleModifDialog.value = value.value; }"
        @close="closeDialog" />

</template>

<script setup>
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import DialogCalcul from '../Dialog/DialogModifSprint.vue';
import { ref, defineProps } from 'vue';
import { Pencil } from 'lucide-vue-next';

const sprints = ref([]);
const index = ref(0);
let visibleModifDialog = ref(false);

const props = defineProps({
    sprints: Array,
    index: Number
});
sprints.value = props.sprints;
index.value = props.index;
const currentSprint = sprints.value[index.value];

let startDate = currentSprint.startDate;
let endDate = currentSprint.endDate;
let sprintEndType = currentSprint.sprintEndType;

// Fonction permettant de formatter la date
const formatDate = (date) => {
    // On sépare les éléments de la date
    const [year, month, day] = date.split('-');
    // On retourne la date formatée
    return `${day}/${month}/${year}`;
};

// Fonction qui permettant de formatter le type de fin de sprint
const formatSprintEndType = (sprintEndType) => {
    // On retourne le type de fin de sprint
    return sprintEndType === 'PRESENTATION' ? 'Présentation' : 'Revue de sprint';
};

const openDialog = () => {
    visibleModifDialog.value = true;
};

const closeDialog = () => {
    visibleModifDialog.value = false;
};

</script>