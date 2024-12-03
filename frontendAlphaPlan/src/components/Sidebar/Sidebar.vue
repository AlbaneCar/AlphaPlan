<template>
    <div id='sidebar' class="sticky min-w-fit min-h-screen bg-custom-darkblueAP ease-in-out transition-all flex flex-col gap-3 p-3 text-white">

        <!-- Header -->
        <div class="flex flex-row items-center justify-between sidebar-element">
            <img id="logo-sidebar" class="w-10 ease-in-out duration-100" src="../../assets/images/logo.svg">
            <p class="font-CASlalomBold text-xl px-5">AlphaPlan</p>
            <div class="bg-[#332466] hover:bg-custom-sideBarActiveBG rounded-md">
                <ChevronLeft class="h-10 w-10 p-2 ease-in-out duration-300 transition-all cursor-pointer"
                :class="{'rotate-180': !state}"
                @click="handleClick" />
            </div>
        </div>

        <!-- Ligne -->
        <div class="w-full h-px bg-custom-sideBarLine"></div>


        <div class="overflow-y-auto gap-3 flex flex-col max-h-full scrollbar">
            <!-- Section 1 : Vue d'ensemble -->
            <h2 v-if="state" class="text-custom-sideBarTitles font-CASlalomBold text-xs mt-3">VUE D'ENSEMBLE</h2>
            <!-- Tableau de bord -->
            <SidebarButton :name="'Tableau de bord'" :route="routeNames.HOME_PAGE"
            v-tooltip="{value:'Tableau de bord', disabled: state, placement: 'right'}" />

            <!-- Section 2 : Mon espace -->
            <h2 v-if="state && PERMISSIONS.MON_ESPACE_SECTION" class="text-custom-sideBarTitles font-CASlalomBold text-xs mt-3">MON ESPACE</h2>
            <div v-else-if="!state && PERMISSIONS.MON_ESPACE_SECTION" class="w-full h-px bg-custom-sideBarLine"></div>
            <!-- Étudiants -->
            <SidebarButton :name="'Étudiants'" :route="routeNames.ETUDIANTS"
            v-tooltip="{value:'Étudiants', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.ETUDIANTS_VUE" />
            <!-- Équipes -->
            <SidebarButton :name="'Équipes'" :route="routeNames.LISTE_EQUIPES_VUE"
            v-tooltip="{value:'Équipes', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.LISTE_EQUIPES_VUE" />
            <!-- Notes -->
            <SidebarButton :name="'Notes'" :route="routeNames.NOTES"
            v-tooltip="{value:'Notes', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.NOTES_VUE" />

            <!-- Section 3 : Mon projet -->
            <h2 v-if="state && PERMISSIONS.MON_PROJET_SECTION" class="text-custom-sideBarTitles font-CASlalomBold text-xs mt-3">MON PROJET</h2>
            <div v-else-if="!state && PERMISSIONS.MON_PROJET_SECTION" class="w-full h-px bg-custom-sideBarLine"></div>
            <!-- Gestion des étudiants -->
            <SidebarButton :name="'Gestion des étudiants'" :route="routeNames.ETUDIANTS"
            v-tooltip="{value:'Gestion des étudiants', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.MON_PROJET_SECTION" />
            <!-- Gestion des équipes -->
            <SidebarButton :name="'Gestion des équipes'" :route="routeNames.CREATETEAM"
            v-tooltip="{value:'Gestion des équipes', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.MON_PROJET_SECTION && (avancee > 0)"/>
            <!-- Gestion des sprints -->
            <SidebarButton :name="'Gestion des sprints'" :route="routeNames.SPRINT"
            v-tooltip="{value:'Gestion des sprints', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.MON_PROJET_SECTION && (avancee > 1)"/>
            <!-- Gestion des échelles -->
            <SidebarButton :name="'Gestion des échelles'" :route="routeNames.ECHELLE_NOTES"
            v-tooltip="{value:'Gestion des échelles', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.MON_PROJET_SECTION && (avancee == 3)"/>
            <!-- Notes -->
            <SidebarButton :name="'Gestion des notes'" :route="routeNames.NOTES"
            v-tooltip="{value:'Gestion des notes', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.MON_PROJET_SECTION && (avancee == 3)" />

            <!-- Section 4 : Mes actions -->
            <h2 v-if="state && PERMISSIONS.MES_ACTIONS_SECTION" class="text-custom-sideBarTitles font-CASlalomBold text-xs mt-3">MES ACTIONS</h2>
            <div v-else-if="!state && PERMISSIONS.MES_ACTIONS_SECTION" class="w-full h-px bg-custom-sideBarLine"></div>
            <!-- Établir un ordre -->
            <SidebarButton :name="'Établir un ordre'" :route="routeNames.PRESENTATION_FORM" :teamId="teamId"
            v-tooltip="{value:'Établir un ordre', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.ETABLIR_ORDRE_VUE"/>
            <!-- Évaluer un sprint -->
            <SidebarButton :name="'Évaluer un sprint'" :route="routeNames.NOTATION_EQUIPE"
            v-tooltip="{value:'Évaluer un sprint', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.EVALUER_SPRINT_VUE"/>
            <!-- Attribuer des bonus/malus -->
            <SidebarButton id="BmSsSideBarButton" :name="'Attribuer des bonus/malus'" :route="routeNames.BONUS_MALUS_SS"
            v-tooltip="{value:'Attribuer des bonus/malus', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.BONUS_MALUS_SS_VUE"/>
            <SidebarButton :name="'Attribuer des bonus/malus'" :route="routeNames.BONUS_MALUS_TM" :teamId="teamId"
            v-tooltip="{value:'Attribuer des bonus/malus', disabled: state, placement: 'right'}"
            v-if="PERMISSIONS.BONUS_MALUS_TM_VUE"/>
            <SidebarButton :name="'Valider des bonus/malus'" :route="routeNames.BONUS_MALUS_TM" :teamId="teamId"
            v-if="PERMISSIONS.BONUS_MALUS_TM_VUE_VALIDATION_SS"/>
        </div>

        <!-- Crédits -->
        <div class="flex flex-col items-center sidebar-element justify-center m-auto mb-0 w-full">
            <p class="text-gray-300 font-CASlalomRegular text-xs flex gap-1 items-end justify-center">CRÉÉ POUR<a href="https://eseo.fr/"><img class="w-5" src="../../assets/images/logoeseo.png"></a></p>
            <p v-tooltip.top="{
                        value: `Copyright (c) for portions of Lucide are held by Cole Bemis 2013-2022 as part of Feather (MIT). All other copyright (c) for Lucide are held by Lucide Contributors 2022.`,
                        pt: {
                            arrow: {
                                style: {
                                    borderBottomColor: 'var(--danger-color)'
                                }
                            },
                            text: 'bg-danger font-Roboto text-xs py-1 text-center'
                        }
                    }"
                    class="text-[10px] text-center text-gray-300 font-CASlalomRegular">Copyright ©</p>
        </div>

        <!-- Ligne -->
        <div class="w-full h-px order-last bg-custom-sideBarLine"></div>

        <!-- Footer -->
        <div class="flex flex-row items-center order-last justify-between w-full">
            <div class="sidebar-element mr-0 w-fit">
                <p class="font-bold flex flex-row items-center justify-between gap-2 h-fit ">
                    {{ username }}<component :is="getComponentByTeam(teamId)" class="h-5" />
                </p>
                <p class="italic w-fit text-xs">{{ tradRoles }}</p>
            </div>
            <router-link :to="{ name: routeNames.LOGIN }">
                <LogOut  class="h-10 w-10 rounded-md p-2 bg-[#332466] hover:bg-custom-sideBarActiveBG ease-in-out duration-200 cursor-pointer" @click="logout"/>
            </router-link>
        </div>
    </div>

    <!-- Chat -->
    <Suspense>
        <ChatButton v-if="avancee == 3" />
    </Suspense>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { ChevronRight, ChevronLeft, LayoutDashboard, Users, LogOut, BookCheck, ShieldCheck, ShieldMinus } from 'lucide-vue-next';

import { logout } from '../../services/JwtService.js';

import routeNames from '../../name'
import SidebarButton from './SidebarButton.vue';
import ChatButton from '../Chat/ChatButton.vue';
import { fetchDataProjectCreation } from '../../services/HomePageService.js';


// Roles
const ROLES = {
    PROJECT_LEADER: 'PROJECT_LEADER',
    OPTION_LEADER: 'OPTION_LEADER',
    SUPERVISING_STAFF: 'SUPERVISING_STAFF',
    TEAM_MEMBER: 'TEAM_MEMBER',
    TECHNICAL_COACHES: 'TECHNICAL_COACHES',
    OPTION_STUDENT: 'OPTION_STUDENT',
};


// Variables
const nom = localStorage.getItem('nom');
const prenom = localStorage.getItem('prenom');
const roles = JSON.parse(localStorage.getItem('roles'));

const currentUserRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));
const tradRoles = rolesToTrad(currentUserRole);

const teamId = localStorage.getItem('teamId');
const etudiantId = localStorage.getItem('user_id');

let state = ref(true);
const username = (nom == 'admin') ? 'Admin' : `${nom} ${prenom}`;

const avancee = ref(0);
const PERMISSIONS = ref({});


// Fonction pour récupérer les données de la création du projet
async function fetchProjectCreationData() {
    const response = await fetchDataProjectCreation();
    avancee.value = response;
}


// On récupère les données de l'utilisateur
onMounted(async () => {
    await fetchProjectCreationData();
    console.log("avancee : ", avancee.value);
    setPermissions();
});


// Fonction pour définir les permissions
const setPermissions = () => {
    PERMISSIONS.value = {
    MON_ESPACE_SECTION: 
        (avancee.value == 3 && (
        currentUserRole.includes('OPTION_LEADER') || 
        currentUserRole.includes('SUPERVISING_STAFF') || 
        currentUserRole.includes('TECHNICAL_COACH') || 
        currentUserRole.includes('TEAM_MEMBER')))  ||
        (avancee.value > 0 && currentUserRole.includes('SUPERVISING_STAFF')),
    ETUDIANTS_VUE: 
        currentUserRole.includes('OPTION_LEADER'),
    LISTE_EQUIPES_VUE:
        (avancee.value == 3 && (
        currentUserRole.includes('OPTION_LEADER') || 
        currentUserRole.includes('TECHNICAL_COACH') ||
        currentUserRole.includes('TEAM_MEMBER'))) ||
        (avancee.value > 0 && currentUserRole.includes('SUPERVISING_STAFF')),
    NOTES_VUE: 
        avancee.value == 3 && (
        currentUserRole.includes('OPTION_LEADER') || 
        currentUserRole.includes('SUPERVISING_STAFF') || 
        currentUserRole.includes('TEAM_MEMBER')
    ),

    MON_PROJET_SECTION: 
        currentUserRole.includes('PROJECT_LEADER'),

    MES_ACTIONS_SECTION: 
        avancee.value == 3 && (
        currentUserRole.includes('SUPERVISING_STAFF') || 
        currentUserRole.includes('TEAM_MEMBER')
    ),
    ETABLIR_ORDRE_VUE: 
        avancee.value == 3 && 
        currentUserRole.includes('TEAM_MEMBER'),
    EVALUER_SPRINT_VUE: 
        avancee.value == 3 && (
        currentUserRole.includes('SUPERVISING_STAFF') || 
        currentUserRole.includes('TEAM_MEMBER')
    ),
    BONUS_MALUS_SS_VUE: 
        avancee.value == 3 &&
        currentUserRole.includes('SUPERVISING_STAFF'),
    BONUS_MALUS_TM_VUE: 
        avancee.value == 3 &&
        currentUserRole.includes('TEAM_MEMBER'),
    BONUS_MALUS_TM_VUE_VALIDATION_SS: 
        avancee.value == 3 &&
        currentUserRole.includes('SUPERVISING_STAFF') && localStorage.teamId != 'null',
    };
}

// Fonction pour changer l'état de la sidebar
const handleClick = () => {
    // On change l'état de la sidebar
    state.value = !state.value;

    // On récupère #logo-sidebar pour changer sa taille et le cacher ou l'afficher
    document.getElementById('logo-sidebar').classList.remove(!state.value ? 'w-10' : 'w-0');
    document.getElementById('logo-sidebar').classList.add(!state.value ? 'w-0' : 'w-10');

    // On récupère les éléments ".sidebar-element p" pour les cacher ou les afficher
    document.querySelectorAll('.sidebar-element p').forEach((element) => {
        element.classList.remove(state.value ? 'hidden' : 'block');
        element.classList.add(state.value ? 'block' : 'hidden');
    });
};

function getComponentByTeam(teamId) {
    return (teamId != 'null') ? ShieldCheck : ShieldMinus;
}

function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}

function rolesToTrad(roles) {
    let trad = '';
    if (roles.includes('PROJECT_LEADER')) trad += 'Chef de projet/';
    if (roles.includes('OPTION_LEADER')) trad += 'Chef d\'option/';
    if (roles.includes('SUPERVISING_STAFF')) trad += 'Encadrant/';
    if (roles.includes('TEAM_MEMBER')) trad += 'Membre d\'équipe/';
    if (roles.includes('TECHNICAL_COACHES')) trad += 'Coach technique/';
    if (roles.includes('JURY_MEMBER')) trad += 'Membre du jury/';
    if (roles.includes('OPTION_STUDENT')) trad += 'Etudiant/';
    return trad.slice(0, -1);
}

</script>

