<template>
    <div id="sidebar" className="w-64 min-w-64 h-screen relative bg-custom-darkblueAP flex flex-col justify-between ease-in-out transition-all duration-300">
        <div class="h-16">
            <div class="flex ml-6 h-full">
                <img class="w-8 mt-auto mb-auto" src="../assets/images/logo.svg">
                <h1 class="text-base font-CASlalomBold text-white ml-2 mt-auto mb-auto">AlphaPlan</h1>
            </div>
        </div>

        <div class="shrink ml-6 mt-8">
            <h2 class="text-custom-sideBarTitles font-CASlalomBold text-xs">VUE D'ENSEMBLE</h2>
        </div>
        <div class="flex items-center ml-6 mt-3">
            <router-link :to="{ name: routeNames.HOME_PAGE }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <LayoutDashboard />
                    Tableau de Bord
                </div>
            </router-link>
        </div>

        <div class="shrink ml-6 mt-8">
            <h2 class="text-custom-sideBarTitles font-CASlalomBold text-xs">MON ESPACE</h2>
        </div>
        <div class="flex items-center ml-6 mt-3" v-if="currentUserRole.includes('OPTION_LEADER')">
            <router-link :to="{ name: routeNames.ETUDIANTS }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <PersonStanding />
                    Étudiants
                </div>
            </router-link>
        </div>
        <div class="flex items-center ml-6 mt-3"
            v-if="reglesAffichage">
            <router-link :to="{ name: routeNames.LISTE_EQUIPES_VUE }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <Users />
                    Équipes
                </div>
            </router-link>
        </div>
        <div class="flex items-center ml-6 mt-3"
            v-if="currentUserRole.includes('OPTION_LEADER') || currentUserRole.includes('SUPERVISING_STAFF') || currentUserRole.includes('TEAM_MEMBER')">
            <router-link
                :to="{ name: ((currentUserRole.includes('TEAM_MEMBER')) ? routeNames.MES_NOTES : routeNames.NOTES) }" class="w-full" v-slot="{ isExactActive }">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <BookCheck />
                    Notes
                </div>
            </router-link>
        </div>

        <div class="shrink ml-6 mt-8" v-if="currentUserRole.includes('OPTION_LEADER')">
            <h2 class="text-custom-sideBarTitles font-CASlalomBold text-xs">MON PROJET</h2>
        </div>
        <div class="flex items-center ml-6 mt-3" v-if="currentUserRole.includes('OPTION_LEADER')">
            <router-link :to="{ name: routeNames.CREATETEAM }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <UserPlus />
                    Création d'équipes
                </div>
            </router-link>
        </div>
        <div class="flex items-center ml-6 mt-3" v-if="currentUserRole.includes('OPTION_LEADER')">
            <router-link :to="{ name: routeNames.SPRINT }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <PackagePlus />
                    Création des sprints
                </div>
            </router-link>
        </div>

        <div class="shrink ml-6 mt-8"
            v-if="currentUserRole.includes('SUPERVISING_STAFF') || currentUserRole.includes('TEAM_MEMBER')">
            <h2 class="text-custom-sideBarTitles font-CASlalomBold text-xs">MES ACTIONS</h2>
        </div>
        <div class="flex items-center ml-6 mt-3" v-if="currentUserRole.includes('TEAM_MEMBER')">
            <router-link :to="{ name: routeNames.PRESENTATION_FORM }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <ListOrdered />
                    Établir un ordre
                </div>
            </router-link>
        </div>
        <div class="flex items-center ml-6 mt-3" v-if="currentUserRole.includes('SUPERVISING_STAFF')">
            <router-link :to="{ name: routeNames.NOTATION_EQUIPE }" v-slot="{ isExactActive }"
                class="w-full">
                <div :class="['flex items-center text-white font-Roboto hover:bg-custom-sideBarActiveBG hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5 gap-2 font-bold',
                    isExactActive ? 'bg-custom-sideBarActiveBG' : '']">
                    <FileDigit />
                    Noter un sprint
                </div>
            </router-link>
        </div>

        <!--
        <div class="shrink ml-6 mt-8">
            <h2 class="text-custom-sideBarTitles font-CASlalomBold text-xs">TEST</h2>
        </div>
        <div class="flex items-center ml-6 mt-3">
            <router-link :to="{ name: routeNames.TEST}"
                class="flex items-center text-custom-darkblueAP font-Roboto hover:bg-blue-200 hover:transition-colors text-sm py-2 px-3 rounded-md w-4/5">
                <img class="w-5 mr-1" src="../assets/icons/test.svg" alt="Icône Jury">
                Test
            </router-link>
        </div>
        -->

        <div class="flex m-auto mb-1">
            <p class="font-CASlalomRegular text-sm text-gray-300">MADE FOR </p>
            <a href="https://eseo.fr/">
                <img class="w-5 ml-2" src="../assets/images/logoeseo.png">
            </a>
        </div>
        <div class="h-16 px-2 mt-3">
            <div class="w-full h-px bg-custom-sideBarLine"></div>
            <div class="h-full flex px-4 justify-between">
                <div class="flex flex-col justify-center items-start">
                    <p class="font-CASlalomBold text-white text-sm">{{ nom }} {{ prenom }}</p>
                    <p class="font-CASlalomRegular text-white text-xs">{{ currentUserRole }}</p>
                </div>
                <LogoutButton class="text-white" />
            </div>
        </div>
    </div>
</template>

<script setup>
import LogoutButton from '../components/LogoutButton.vue';
import routeNames from '../name'
import { LayoutDashboard, Users, PersonStanding, BookCheck, UserPlus, PackagePlus, FileDigit, ListOrdered } from 'lucide-vue-next';
import Sidebar from './Sidebar/Sidebar.vue';
import SidebarButton from './Sidebar/SidebarButton.vue';
import { ref, computed, onMounted } from 'vue';
import { fetchEquipes } from '../services/EquipesService';

// Roles
const ROLES = {
    PROJECT_LEADER: 'PROJECT_LEADER',
    OPTION_LEADER: 'OPTION_LEADER',
    SUPERVISING_STAFF: 'SUPERVISING_STAFF',
    TEAM_MEMBER: 'TEAM_MEMBER',
    TECHNICAL_COACHES: 'TECHNICAL_COACHES',
    JURY_MEMBER: 'JURY_MEMBER',
    OPTION_STUDENT: 'OPTION_STUDENT',
};

// Variables
const nom = localStorage.getItem('nom');
const prenom = localStorage.getItem('prenom');
const currentUserRole = ROLES[JSON.parse(localStorage.getItem('roles'))];
const etatEquipes = ref();

onMounted(async () => {
  try {
    const equipes = await fetchEquipes();
    etatEquipes.value = equipes[0].etatEquipes;
  } catch(error){
  }
});

const reglesAffichage = computed(() => {
    if (etatEquipes.value === 'GENERE') {
        return currentUserRole.includes('OPTION_LEADER');
    } else if (etatEquipes.value === 'PREPUBLIE') {
        return currentUserRole.includes('OPTION_LEADER') || currentUserRole.includes('SUPERVISING_STAFF');
    } else if (etatEquipes.value === 'PUBLIE') {
        return currentUserRole.includes('OPTION_LEADER') || currentUserRole.includes('SUPERVISING_STAFF') || currentUserRole.includes('TEAM_MEMBER');
    }
    return false;
});
</script>
