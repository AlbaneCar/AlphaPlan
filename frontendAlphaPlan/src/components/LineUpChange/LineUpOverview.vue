<template>
<Suspense>
    <div class="card border border-custom-lightblueAP bg-white rounded-md shadow-sm">
        <Accordion :activeIndex="0">
            <AccordionTab>
                <template #header>
                    <div class="flex flex-row items-center justify-start gap-4">
                        <p v-if="userRole.includes('TEAM_MEMBER')">Proposer un changement de line-up / Valider un changement de line-up</p>
                        <p v-else>Propositions de changement de line-up</p>
                    </div>
                </template>
                <div class="flex flex-row w-full h-fit justify-between items-center">
                    <DropdownLineUp v-if="userRole.includes('OPTION_STUDENT') || userRole.includes('TEAM_MEMBER')"/>
                    <div class="border w-0 min-h-32 mr-10" v-if="userRole.includes('OPTION_STUDENT') || userRole.includes('TEAM_MEMBER')" ></div>
                    <CarouselLineUp :teamState="teamState"
                    @eventCarousel="handleCustomEvent"/>
                </div>
            </AccordionTab>
        </Accordion>
    </div>
</Suspense>
</template>

<script setup>
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';
import CarouselLineUp from './CarouselLineUp.vue';
import DropdownLineUp from './DropdownLineUp.vue';
import { ref } from 'vue';

// Props pour l'état des équipes 'teamState' avec comme valeur par défaut 'PREBUBLIE'
const props = defineProps({
    teamState: {
        type: String,
        default: 'PREBUBLIE'
    }
});


// Variables
let userRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));
const emit = defineEmits(['eventLineUp']);
const teamState = ref(props.teamState);


// Fonction pour construire une chaîne de caractères des rôles de l'utilisateur
function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}

</script>
