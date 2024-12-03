import { createWebHistory, createRouter } from 'vue-router'
import { isLogged } from '../services/JwtService'

import routeNames from '../name'
import routeViews from '../views'

import Sidebar from '../components/Sidebar/Sidebar.vue'
import CreateTeam from '../views/CreateTeam.vue'
import SprintFormulaire from '../views/SprintFormulaire.vue'
import HomePage from '../views/HomePage.vue'
import PresentationForm from '../views/PresentationForm.vue'
import Etudiants from '../views/Etudiants.vue'
import EtudiantDetails from '../views/EtudiantDetails.vue'
import LoginView from "../views/LoginView.vue"
import TestView from "../views/TestView.vue"
import RedirectView from "../views/RedirectView.vue"
import ListeEquipesN from '../views/ListeEquipesN.vue'
import ListeEquipes from '../views/ListeEquipes.vue'
import NotationGlobal from '../views/NotationGlobal.vue'
import bmSS from '../views/bmSS.vue'
import NotesSprint from '../views/NotesSprint.vue'
import echelleNotes from '../views/EchelleNote.vue'
import Signalement from '../views/Signalement.vue'
import BonusMalusTeamMembers from '../views/BonusMalusTeamMembers.vue'
import QuestionsReponses from '../views/QuestionsReponses.vue'
import { customFetch } from '../utils/FetchUtil'

// Variable pour les rôles
const ROLES = {
  PROJECT_LEADER: 'PROJECT_LEADER',
  OPTION_LEADER: 'OPTION_LEADER',
  SUPERVISING_STAFF: 'SUPERVISING_STAFF',
  TEAM_MEMBER: 'TEAM_MEMBER',
  TECHNICAL_COACHES: 'TECHNICAL_COACHES',
  JURY_MEMBER: 'JURY_MEMBER',
  OPTION_STUDENT: 'OPTION_STUDENT',
};

// Architecture of the router
const routes = [
  { path: '/', redirect: '/login' },

  {
    path: '/login',
    name: routeNames.LOGIN,
    components: {
      [routeViews.MAIN]: LoginView,
    },
    meta : { auth : false }
  },

  {
    path: '/redirect',
    name: routeNames.REDIRECT,
    components: {
      [routeViews.MAIN]: RedirectView,
    },
    meta : { auth : false }
  },

  {
    path: '/notes',
    name: routeNames.NOTES,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN]: NotesSprint,
    },
    meta : { auth : true }
  },

  {
    path: '/questionsReponses',
    name: routeNames.QUESTIONS,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN]: QuestionsReponses,
    },
    meta : { auth : true }
  },

  {
    path: '/test',
    name: routeNames.TEST,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN]: TestView,
    },
    meta : { auth : false }
  },

  {
    path: '/equipes',
    children: [
      {
        path: '',
        name: routeNames.LISTE_EQUIPES_VUE,
        components: {
          [routeViews.SIDEBAR]: Sidebar,
          [routeViews.MAIN]: ListeEquipesN,
        }
      },
      {
        path: ':teamId',
        children: [
          {
            path: 'presentation',
            name: routeNames.PRESENTATION_FORM,
            components: {
              [routeViews.SIDEBAR]: Sidebar,
              [routeViews.MAIN]: PresentationForm,
            }
          },
          {
            path: 'bonusmalusTM',
            name: routeNames.BONUS_MALUS_TM,
            components: {
              [routeViews.SIDEBAR]: Sidebar,
              [routeViews.MAIN]: BonusMalusTeamMembers,
            }
          }
        ]
      },
      {
        path: '/etudiants',
        children: [
          {
            path: '',
            name: routeNames.ETUDIANTS,
            components: {
              [routeViews.SIDEBAR]: Sidebar,
              [routeViews.MAIN]: Etudiants,
            },
            meta : { auth : true }
          },
          {
            path: ':etudiantId',
            name: routeNames.ETUDIANTS_PARAMS,
            components: {
              [routeViews.SIDEBAR]: Sidebar,
              [routeViews.MAIN]: EtudiantDetails,
            },
            meta : { auth : true }
          },
        ]
      },
    ],
    meta : { auth : true }
  },

  {
    path: '/createteam',
    name: routeNames.CREATETEAM,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN]: CreateTeam,
    },
    meta : { auth : true }
  },

  {
    path : '/sprint',
    name: routeNames.SPRINT,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN] : SprintFormulaire,
    },
    meta : { 
      auth : true,
      role : [
        ROLES.PROJECT_LEADER, 
        ROLES.OPTION_LEADER
      ]
    }
  },

  {
    path : '/bmSS',
    name: routeNames.BONUS_MALUS_SS,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN] : bmSS,
    },
    meta : { auth : true }
  },


  {
    path : '/notationEquipe',
    name: routeNames.NOTATION_EQUIPE,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN] : NotationGlobal,
    },
    meta : { 
      auth : true,
      role : [
        ROLES.SUPERVISING_STAFF,
        ROLES.TEAM_MEMBER,
        ROLES.TECHNICAL_COACHES,
        ROLES.JURY_MEMBER,
      ] 
    }
  },

  {
    path : '/echelleNotes',
    name: routeNames.ECHELLE_NOTES,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN] : echelleNotes,
    },
    meta : { auth : true }
  },

  {
    path : '/HomePage',
    name: routeNames.HOME_PAGE,
    components: {
      [routeViews.SIDEBAR]: Sidebar,
      [routeViews.MAIN] : HomePage,
    },
    meta : { auth : true }
  },

  {
    path : '/signalement',
    name: routeNames.SIGNALEMENT,
    components: {
      [routeViews.MAIN] : Signalement,
    },
    meta : { auth : true }
  },

  {
    path : '/signalement',
    name: routeNames.SIGNALEMENT,
    components: {
      [routeViews.MAIN] : HomePage,
    },
    meta : { auth : true }
  },

  {
    path : '/:pathMatch(.*)*',
    redirect: '/HomePage',
  },
  
]

const router = createRouter({
  history: createWebHistory(),
  routes,
})

router.beforeEach((to, from, next) => {
    if (to.meta?.auth && !isLogged()) {
        next({ name: routeNames.LOGIN });
    } else if (to.meta?.role){
        customFetch('users/roles')
        .then(response => response.json())
        .then(data => {
            console.log("role : ", data)
            if(to.meta?.role.some(role => data.includes(role))) {
                next();
            } else {
                next({ name: routeNames.HOME_PAGE });
            }
        })
    } else {
        next();
    }
})

export default router;
