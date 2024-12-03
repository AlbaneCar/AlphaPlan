<script setup>
import { ref, watch } from 'vue';
import { MessageCircle, MessageCircleOff, Plus, Trash2 } from 'lucide-vue-next';
import { getConversations, suppConversation, getConversationsUser } from '../../services/ConversationsService.js';
import { useToast } from 'vue-toastification';
import { fetchEquipes, fetchEtudiants } from '../../services/EquipesService.js';
import { createConv } from '../../services/ConversationsService.js';
import ChatHistory from './ChatHistory.vue';
import ChatBox from './ChatBox.vue';
import MultiSelect from 'primevue/multiselect';
import { createNotification } from '../../services/NotificationService.js';

// Variables
const expanded = ref(false);
const toast = useToast();
const value = ref();

// Données
const equipes = ref([]);
const etudiants = ref([]);
const options = ref([]);
const participants = ref([]);
const conversations = ref([]);

// Contrôle de l'affichage des Dialogs
const selectedConversation = ref(null);
const showCreateConversation = ref(false);
const showDeleteConversation = ref(false);

// On récupère le rôle de l'utilisateur
let userRole = buildRoleString(JSON.parse(localStorage.getItem('roles')));
let userId = localStorage.getItem('user_id');
userId = parseInt(userId);

function buildRoleString(roles) {
    let roleString = '';
    roles.forEach(role => {
        roleString += role + '/';
    });
    return roleString;
}

// Récupération des équipes
const getTeams = async () => {
    const response = await fetchEquipes();
    equipes.value = response;
}

// En fonction du rôles de l'utilisateur, on récupère les conversations
const loadConversations = async () => {
    if (userRole.includes('PROJECT_LEADER') || userRole.includes('OPTION_LEADER') || userRole.includes('SUPERVISING_STAFF')) {
        const response = await getConversations();
        conversations.value = response;
        // Si des équipes existent, on les récupère
        await getTeams();
    } else {
        const response = await getConversationsUser(userId);
        conversations.value = response;
    }
    // Si des conversations existent, sélectionnez la première par défaut
    if (conversations.value.length > 0) {
        selectedConversation.value = conversations.value[0];
    } else {
        selectedConversation.value = null;
    }
}

// Fonction permettant de récupérer les données
async function loadParticipants() {
    // Récupération des Étudiants
    let response = await fetchEtudiants();
    etudiants.value = response;
    // Récupération des Équipes
    response = await fetchEquipes();
    equipes.value = response;

    // On va créer une liste au format JSON pour l'autocomplete avec les Étudiants et les Équipes au format JSON :
    // {type: "Étudiant ou Équipe", items: [{nom: "nom/prénom de l'Étudiant ou de l'Équipe", id: "id de l'Étudiant ou de l'Équipe"}]}
    let temp = [];
    // On va ajouter les Équipes
    let equipesJSON = {label: "Équipe", items: []};
    equipes.value.forEach((equipe) => {
        equipesJSON.items.push({label: equipe.nom, id: equipe.id, type: "team"});
    });
    temp.push(equipesJSON);
    // On va ajouter les Étudiants
    let etudiantsJSON = {label: "Étudiant", items: []};
    etudiants.value.forEach((etudiant) => {
        etudiantsJSON.items.push({label: etudiant.nom + " " + etudiant.prenom, id: etudiant.id, type: "student"});
    });
    temp.push(etudiantsJSON);
    // On va ajouter les données à la liste des options
    options.value = temp;
}


// On charge les participants potentiels
await loadParticipants();

// On charge les conversations
await loadConversations();

// Fonction pour gérer l'expansion du chat
const handleClick = () => {
    expanded.value = !expanded.value;
    if (!expanded.value) showCreateConversation.value = false;
}

// Fonction pour mettre à jour la conversation sélectionnée
const updateSelectedConversation = (newConversation) => {
    selectedConversation.value = newConversation;
}

// Fonction qui permet d'afficher le Dialog de création de conversation
const showCreateConversationInput = () => {
    if (!showDeleteConversation.value) showCreateConversation.value = !showCreateConversation.value;
}

// Fonction qui permet d'afficher le Dialog de suppression de conversation
const showDeleteConversationInput = () => {
    if (!showCreateConversation.value) showDeleteConversation.value = !showDeleteConversation.value;
}

//Fonction permettant de créer une conversation
const createConversation = async () => {
    // Récupérer les informations de la conversation
    const conversationName = document.getElementById('conversationName').value;
    try {
        // On va créer une conversation avec les participants sélectionnés
        let response = await createConv(conversationName, userId, participants.value);
        if (response.conversation != null) {
            toast.success(response.message);
            participants.value.forEach(async (participant) => {
                if (participant != userId) {
                    // On va envoyer une notification à chaque participant
                    let response2 = await createNotification("NOUVELLE_CONVERSATION", "Vous avez été ajouté à la conversation '" + conversationName + "'", userId, participant);
                    if (response2.notification == null) {
                        toast.error("Erreur lors de l'envoi de la notification !\n" + response2.message);
                    }
                }
            });
        } else {
            toast.error("Erreur lors de la création de la conversation !\n" + response.message);
        }
    } catch (error) {
        toast.error("Erreur lors de la création de la conversation !\n" + error.message);
    }
    // On met à jour la liste des conversations
    await loadConversations();
    // On ferme le Dialog
    showCreateConversation.value = false;
}

//Fonction permettant de supprimer une conversation
const deleteConversation = async () => {
    // Supprimer la conversation
    let response = await suppConversation(selectedConversation.value.id);
    // Envoyer une notification à chaque participant
    if (response) {
        toast.success("Conversation supprimée");
        selectedConversation.value.participants.forEach(async (participant) => {
            if (participant.id != userId) {
                let message = "La conversation '" + selectedConversation.value.titre + "' a été supprimée";
                let response2 = await createNotification("CONVERSATION_SUPPRIMEE", message, userId, participant.id);
                if (response2.notification == null) {
                    toast.error("Erreur lors de l'envoi de la notification !\n" + response2.message);
                }
            }
        });
    } else {
        toast.error("Erreur lors de la suppression de la conversation !");
    }
    // On met à jour la liste des conversations
    await loadConversations();
    // On ferme le Dialog
    showDeleteConversation.value = false;
}

// Watcher pour les participants
watch(value, (newValue) => {
  // Tableau contenant les participants
  let temp = [];
  // On ajoute l'utilisateur connecté à la liste des participants
  temp.push(userId);
  // Si l'élément est un Étudiant, on l'ajoute son id à la liste des participants s'il n'est pas déjà présent
  newValue.forEach((element) => {
    if (element.type === "student") {
      if (!temp.includes(element.id)) {
        temp.push(element.id);
      }
    }
  });
  // Si l'élément est une Équipe, on ajoute les id des Étudiants de l'Équipe à la liste des participants s'ils ne sont pas déjà présents
  newValue.forEach((element) => {
    if (element.type === "team") {
      // On va regarder dans la liste des Étudiants leur attribut equipe_id et le comparer à l'id de l'Équipe
      etudiants.value.forEach((etudiant) => {
        if (etudiant.equipe != null) {
          if (etudiant.equipe.id === element.id) {
            if (!temp.includes(etudiant.id)) {
              temp.push(etudiant.id);
            }
          }
        }
      });
    }
  });
  // Affichage de la liste des participants
  participants.value = temp;
});
</script>

<template>
    <!-- Fênetre de chat -->
    <div :class="{'p-3 w-80 min-h-72 h-fit': expanded}"
    class="absolute top-0 right-0 z-50 bg-custom-darkblueAP m-2 flex flex-col justify-between items-center rounded-md transition-all duration-200 ease-in-out">

        <!-- Header de la fenêtre -->
        <div class="flex flex-row justify-between items-center w-full gap-3">

            <!-- Bouton pour ouvrir/fermer le chat -->
            <MessageCircle v-if="!expanded" color="white" class="min-h-10 min-w-10 p-2 rounded-md hover:bg-custom-sideBarActiveBG ease-in-out duration-200 cursor-pointer" @click="handleClick"/>
            <MessageCircleOff v-else color="white" class="min-h-10 min-w-10 rounded-md p-2 hover:bg-custom-sideBarActiveBG ease-in-out duration-200 cursor-pointer order-last" @click="handleClick"/>

            <div v-if="expanded && (userRole.includes('PROJECT_LEADER') || userRole.includes('SUPERVISING_STAFF'))" class="bg-custom-darkblueAP rounded-md absolute top-0 -left-2 -translate-x-full h-fit p-3 justify-between gap-3 flex flex-col">
                <!-- Bouton pour créer une conversation -->
                <Plus color="white" class="min-h-10 min-w-10 rounded-md p-2 hover:bg-custom-sideBarActiveBG ease-in-out duration-200 cursor-pointer"
                @click="showCreateConversationInput"/>
                <!-- Bouton pour supprimer une conversation -->
                <Trash2 color="white" class="min-h-10 min-w-10 rounded-md p-2 hover:bg-custom-sideBarActiveBG ease-in-out duration-200 cursor-pointer" @click="showDeleteConversationInput"/>
            </div>

            <!-- Dropdown avec les conversations -->
            <ChatHistory v-if="expanded" :conversations="conversations" @update:selectedConversation="updateSelectedConversation"/>
        </div>

        <!-- Body de la fenêtre -->
        <div class="min-h-full w-full">
            <!-- Zone d'affichage des messages de la conversation -->
            <ChatBox v-if="expanded" :conversation="selectedConversation"/>
        </div>

        <!-- Dialog pour créer une conversation -->
        <div v-if="(showCreateConversation)" class="absolute z-50 top-0 left-0 h-full w-full rounded-md bg-white bg-opacity-30">
            <div class="absolute w-3/4 h-fit top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-md p-3 flex flex-col gap-3">
                <h1 class="font-bold">Créer une conversation</h1>
                <!-- Nom de la nouvelle conversation -->
                <input id="conversationName" type="text" class="w-full border rounded-md p-2" placeholder="Nom de la conversation"/>
                <!-- Sélection des participants -->
                <MultiSelect v-model="value" :options="options" optionLabel="label" optionGroupLabel="nom" optionGroupChildren="items" display="chip" filter class="max-w-full min-w-full border">
                    <template #optiongroup="slotProps">
                        <div class="flex align-items-center">
                            <div>{{ slotProps.option.label + '(s)' }}</div>
                        </div>
                    </template>
                </MultiSelect>
                <div class="flex flex-row justify-between items-center gap-10">
                    <button class="px-3 py-2 bg-green-500 border-green-500 border-2 text-white rounded-md font-bold hover:bg-green-600 hover:border-green-600 duration-300 ease-in-out" @click="createConversation">Créer</button>
                    <button class="px-3 py-2 bg-white text-red-500 border-red-500 border-2 rounded-md font-bold hover:bg-red-500 hover:text-white duration-300 ease-in-out" @click="showCreateConversationInput">Annuler</button>
                </div>
            </div>
        </div>

        <!-- Dialog pour supprimer une conversation -->
        <div v-if="(showDeleteConversation && selectedConversation)" class="absolute top-0 left-0 h-full w-full rounded-md bg-white bg-opacity-30">
            <div class="absolute w-fit h-fit top-1/2 left-1/2 transform -translate-x-1/2 -translate-y-1/2 bg-white rounded-md p-3 flex flex-col gap-3">
                <h1 class="font-bold">Êtes vous sûr de vouloir supprimer cette conversation ?</h1>
                <div class="flex flex-row justify-between items-center gap-10">
                    <button class="px-3 py-2 bg-red-500 border-red-500 border-2 text-white rounded-md font-bold hover:bg-red-600 hover:border-red-600 duration-300 ease-in-out" @click="deleteConversation">Supprimer</button>
                    <button class="px-3 py-2 bg-white text-red-500 border-red-500 border-2 rounded-md font-bold hover:bg-red-500 hover:text-white duration-300 ease-in-out" @click="showDeleteConversationInput">Annuler</button>
                </div>
            </div>
        </div>
    </div>
</template>