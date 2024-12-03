<script setup>
import TitreSprint from '../components/TitreSprint.vue';
import BoxHome from '../components/BoxHome.vue';
import Title from '../components/Title.vue';
import Illustration from '../components//NoDataIllustration/Illustration.vue';
import Stepper from '../components/Stepper/Stepper.vue';
import NotifBox from '../components/Box/NotifBox.vue';
import { BellRing, CalendarClock } from 'lucide-vue-next';
import { Clock } from 'lucide-vue-next';
import { ref } from 'vue';
import { fetchDataProjectCreation } from '../services/HomePageService';
import { deleteNotification } from '../services/NotificationService';
import { getAllSprints } from '../services/SprintService';
import { getNotifications } from '../services/NotificationService';
import BoxEcheance from '../components/Box/BoxEcheance.vue';

const prenom = ref(localStorage.getItem('prenom'));
const avancee = ref(null);
const role = JSON.parse(localStorage.getItem('roles'))[0] != null ? JSON.parse(localStorage.getItem('roles'))[0] : null;
const sprints = ref(null);
const actualSprint = ref(null);
const notifications = ref(null);


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

// Permissions de la page :
const PERMISSIONS = {
    CONFIGURATION_PROJET: {
        [ROLES.PROJECT_LEADER]: true,
        [ROLES.OPTION_LEADER]: true,
    },
    ACCES_MON_EQUIPE: {
        [ROLES.SUPERVISING_STAFF]: true,
        [ROLES.TEAM_MEMBER]: true,
    },
    ACCES_MES_NOTES: {
        [ROLES.TEAM_MEMBER]: true,
    },
};

// Récupération de l'autorisation de l'utilisateur
function hasPermissionInSection(section, role) {
    console.log("Role" + role, "Section" + section)
    if (PERMISSIONS[section] == null) return false;
    if (PERMISSIONS[section][role] == null) return false;
    else {
        return PERMISSIONS[section][role];
    }
}

async function fetchProjectCreationData() {
    const response = await fetchDataProjectCreation();
    avancee.value = response;
}

async function getAllDataSprint() {
    const response = await getAllSprints();
    sprints.value = response.sort((a, b) => new Date(a.dateDepart) - new Date(b.dateDepart));
    //Date du jour
    const date = new Date();
    const todayWithoutTime = new Date(date.getFullYear(), date.getMonth(), date.getDate());

    //récupérer le sprint actuel
    const currentSprint = response.find(sprint => todayWithoutTime >= new Date(sprint.startDate) && todayWithoutTime <= new Date(sprint.endDate));
    actualSprint.value = currentSprint;
}

async function getAllDataNotifications() {
    //Récupérer l'id de l'utilisateur connecté
    const user_id = localStorage.getItem('user_id');
    const response = await getNotifications(user_id);

    //trier par date décroissante
    response.sort((a, b) => new Date(b.date_signalement) - new Date(a.date_signalement));
    notifications.value = response;
    console.log(notifications.value);
}

async function deleteNotif(notif) {

    try {
        //Supprimer la notification dans la base de données :
        const response = await deleteNotification(notif.id);

        //Supprimer la notification dans la ref notifications :
        notifications.value = notifications.value.filter(notification => notification.id != notif.id);
    }
    catch (error) {
        console.log(error);
    }

}



fetchProjectCreationData();
getAllDataSprint();
getAllDataNotifications();
</script>

<template>
    <Suspense>
        <div class="flex flex-1 flex-col w-full bg-custom-sideBarBG">
            <div v-if="avancee != null && avancee < 3 && hasPermissionInSection('CONFIGURATION_PROJET', role)"
                class="flex-1 p-10 flex flex-col h-screen overflow-y-scroll">
                <!-- Configuration du Projet -->
                <div class="flex flex-col gap-4">
                    <TitreSprint>Bienvenue {{ prenom }} !</TitreSprint>
                    <div>
                        <p class="text-sm text-custom-darkblueAP font-Roboto">
                            Vous êtes actuellement sur le tableau de bord de l'application AlphaPlan. <br><br> Aucun
                            projet n'est pour le moment lancé, mais vous pouvez en paramétrer un en suivant les étapes
                            ci-dessous. <br> Une fois la configuration terminée, vous pourrez alors accéder à l'ensemble
                            des fonctionnalités de gestion de votre projet.
                        </p>
                    </div>
                </div>
                <div class="flex justify-start items-center w-full">
                    <div class="flex flex-col gap-5 mt-16 w-full">
                        <Title>Configuration de votre projet :</Title>
                        <BoxHome v-if="avancee != null" :avancee="avancee" />
                    </div>
                </div>
            </div>
            <div v-else-if="avancee != null && avancee < 3"
                class="flex-1 p-10 flex flex-col h-screen overflow-y-auto justify-center items-center max-h-screen">
                <!-- Page d'attente de lancement de projet -->
                <div class="flex justify-center items-center p-16">
                    <Illustration nameImage="Clock.svg" :width="350" />
                    <Title>Tic, Tac ... <br> Soyez patient {{ prenom }}, <br>le projet n'est pas encore lancé...</Title>
                </div>
            </div>
            <template v-else-if="avancee != null">
                <!-- Dashboard de l'application en cours-->
                <div class="flex flex-1 p-8 flex-col overflow-y-scroll h-screen min-h-screen max-h-screen w-full">
                    <div class="flex flex-col gap-2 w-full h-full max-h-full">
                        <Title>Bienvenue {{ prenom }} !</Title>
                        <p class="text-sm text-custom-darkblueAP font-Roboto mb-5">Vous êtes sur le tableau de bord de
                            l'application AlphaPlan.<br>
                            Retrouvez toutes les informations essentielles du projet GL, et accédez aux fonctionnalités
                            grâce à la barre de navigation.
                        </p>
                        <div class="flex flex-1 flex-row justify-start items-center w-full gap-8 items-stretch">
                            <div class="flex flex-col gap-8 w-full h-full max-h-full">
                                <!-- Chronologie du Projet-->
                                <div
                                    class="flex flex-col bg-white border border-gray-200 shadow-sm rounded-md p-4 w-full gap-3">
                                    <div class="flex justify-start items-center gap-2 ">
                                        <Clock class="w-6 text-custom-darkblueAP" />
                                        <h1 class="text-xl text-custom-darkblueAP font-CASlalomBold">Chronologie du
                                            projet
                                        </h1>
                                    </div>

                                    <div class="flex-1 flex justify-center items-center w-full px-6">
                                        <div class="w-full flex justify-center">
                                            <Stepper :sprints="sprints" :actualSprint="actualSprint" />
                                        </div>

                                    </div>
                                </div>

                                <!-- En Construction -->
                                <div
                                    class="flex-1 flex flex-col bg-white border border-gray-200 shadow-sm h-full rounded-md justify-center items-center gap-2 w-full p-4">
                                    <Illustration nameImage="build.svg" :width="180" />
                                    <div class="text-xl text-custom-darkblueAP font-CASlalomBold">En construction...
                                    </div>
                                </div>
                            </div>
                            <div class="flex flex-col w-fit justify-start items-center h-full max-h-full gap-8">
                                <!-- Notifications -->
                                <div
                                    class="flex h-4/5 flex-col bg-white border border-gray-200 shadow-sm rounded-md w-96 p-4 max-h-full overflow-y-hidden">
                                    <div class="flex justify-start items-center gap-2">
                                        <BellRing class="w-6 text-custom-darkblueAP" />
                                        <h1 class="text-xl text-custom-darkblueAP font-CASlalomBold">Notifications</h1>
                                    </div>
                                    <NotifBox :notifications="notifications"
                                        @deleteNotification="(notif) => deleteNotif(notif)" />
                                </div>
                                <!-- Prochaine Échéance -->
                                <div
                                    class="flex flex-col h-1/5 justify-start items-start bg-white rounded-md shadow-sm border border-gray-200 w-full p-4 gap-2">
                                    <div class="flex justify-start items-center gap-2">
                                        <CalendarClock class="w-6 text-custom-darkblueAP" />
                                        <h1 class="text-xl text-custom-darkblueAP font-CASlalomBold">Échéances</h1>
                                    </div>
                                    <div class="flex flex-col justify-start items-center w-full h-full">
                                        <BoxEcheance :actualSprint="actualSprint" />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </template>
        </div>
    </Suspense>
</template>