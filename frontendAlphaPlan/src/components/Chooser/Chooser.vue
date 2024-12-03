<script setup>
import Possibility from './Possibility.vue';
import { ref } from 'vue';

import { fetchEquipes } from '../../services/EquipesService';
import { getTeamById } from '../../services/PresentationFormService';


const data = ref(null);
const selected = ref(null);

const props = defineProps({
    modelValue: {
        type: Array,
        required: true
    },
    equipes : {
        type: Array,
        required: false
    },
    sprints : {
        type: Array,
        required: false
    },
    type: {
        type: String,
        required: true
    }
});

const equipes = ref(props.equipes);
const sprints = ref(props.sprints);

const emits = defineEmits(['update:modelValue']);

const updateValue = (newValue) => {
    emits('update:modelValue', newValue);
};

const changeVisibility = (clickedElement) => {
    if(props.type == 'EQUIPES'){
        equipes.value.forEach((element) => {
            element.active = (element === clickedElement) ? (element.active === true ? true : !element.active) : false;
        });
    } else if(props.type == 'SPRINTS'){
        sprints.value.forEach((element) => {
            element.active = (element === clickedElement) ? (element.active === true ? true : !element.active) : false;
        });
    }
};

const loadDataEquipes = async () => {
    if(equipes.value.length == 0) return;
    equipes.value.forEach((element) => {
        element.active = false;
    });
    equipes.value[0].active = true;
    selected.value = equipes.value[0];
    const dataElement = await getTeamById(equipes.value[0].id);
    emits('update:modelValue', [equipes.value[0].id, dataElement]);
};

const loadDataSprint = async() => {
    if(sprints.value.length == 0) return;
    sprints.value.forEach((element) => {
        element.active = false;
    });
    sprints.value[0].active = true;
    selected.value = sprints.value[0];
    emits('update:modelValue', [sprints.value[0]]);
};

async function changeElement(element){

    changeVisibility(element);
    if(selected.value.id == element.id) return;

    // Charger les données de l'élément sélectionné

    if(props.type == 'EQUIPES'){
        const dataElement = await getTeamById(element.id);
        updateValue([element.id, dataElement]);
    } else if(props.type == 'SPRINTS'){
        updateValue([element]);
    }
    
    selected.value = element;
}

if(props.type == 'EQUIPES'){
    loadDataEquipes();
} else if(props.type == 'SPRINTS'){
    loadDataSprint();
}

console.log(selected.value);
</script>

<template>
    <div v-if="type == 'EQUIPES' && equipes && equipes.length != 0" class="flex bg-white w-fit p-[5px] px-2 rounded-md gap-2 border border-gray-200 shadow-sm">
        <Possibility v-if="equipes" v-for="equipe in equipes" :key="equipe.nom" :active="equipe.active" @click="changeElement(equipe)" class="cursor-pointer select-none">
            {{ equipe.nom }}
        </Possibility>
    </div>
    <div v-else-if="type == 'SPRINTS' && sprints && sprints.length != 0" class="flex bg-white w-fit p-[5px] px-2 rounded-md gap-2 border border-gray-200 shadow-sm">
        <Possibility v-if="sprints" v-for="sprint in sprints" :key="sprint.id" :active="sprint.active" @click="changeElement(sprint)" class="cursor-pointer select-none">
            {{ sprint.name }}
        </Possibility>
    </div>
</template>