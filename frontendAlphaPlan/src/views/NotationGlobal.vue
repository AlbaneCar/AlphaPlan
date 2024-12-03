<script setup>

import { ref, watch, computed, onMounted } from 'vue';
import Title from '../components/Title.vue';
import Chooser from '../components/Chooser/Chooser.vue';
import BoxNotation from '../components/Box/BoxNotation.vue';
import Button from '../components/Buttons/Button.vue';
import Echelle from '../components/Echelle.vue';
import { saveNoteToDatabase, getNoteFromDatabase, getNoteIndividuelleFromDatabase, saveNoteIndividuelleToDatabase } from '../services/NotationGlobalService';
import { useToast } from "vue-toastification";
import { fetchEquipes } from '../services/EquipesService';
import { getAllSprints } from '../services/SprintService';
import { fetchEchellesNote } from '../services/EchelleNoteService.js';
import Carousel from 'primevue/carousel';
import Accordion from 'primevue/accordion';
import AccordionTab from 'primevue/accordiontab';


// Déclarez une référence pour stocker le numéro de sprint
const sprint = ref([]);

// Déclarez une référence pour stocker la note
const managementNote = ref();
const supportNote = ref();
const managementCommentaire = ref('');
const supportCommentaire = ref('');
const techniqueNote = ref();
const techniqueCommentaire = ref('');
const conformiteNote = ref();
const conformiteCommentaire = ref('');
const globalNote = ref();
const globalCommentaire = ref('');
const individuelNotes = ref([]);
const individuelCommentaires = ref([]);
const toast = useToast();
const formSubmitSuccess = ref(false);
const formSubmitted = ref(false);
const idUtilisateurConnecte = ref(localStorage.getItem("user_id"));

const dataLoaded = ref(false);
const equipe = ref([]);

// Références Equipes & Sprints
const equipes = ref(null);
const sprints = ref(null);


// Fonction pour obtenir et analyser le rôle depuis localStorage
const getRolesFromLocalStorage = () => {
    const roles = localStorage.getItem('roles');
    return roles ? JSON.parse(roles) : [];
};

console.log('localStorage.roles:', getRolesFromLocalStorage());

// Propriété calculée pour vérifier si l'utilisateur est un SUPERVISING_STAFF
const isSupervisingStaff = computed(() => getRolesFromLocalStorage().includes('SUPERVISING_STAFF'));

const idTechnicalCoaches = computed(() => getRolesFromLocalStorage().includes('TECHNICAL_COACHES'));

const isTeamMember = computed(() => getRolesFromLocalStorage().includes('TEAM_MEMBER'));

let supervisingStaffTeamId = localStorage.getItem('teamId');
supervisingStaffTeamId = parseInt(supervisingStaffTeamId);

console.log(supervisingStaffTeamId);


watch([equipe, sprint, idUtilisateurConnecte], async ([newEquipe, newSprint, idUtilisateurConnecte]) => {


    if (newEquipe.length > 0 && newSprint[0] !== undefined && idUtilisateurConnecte !== undefined) {
        const notes = await getNoteFromDatabase(newEquipe[0], newSprint[0].id, idUtilisateurConnecte);

        //creer la récupération des notes individuelles pas la même table que noteEquipe.
        const notesIndividuellesParMembre = [];

        for (const user of newEquipe[1]) {
            const studentNotes = await getNoteIndividuelleFromDatabase(user.id, newSprint[0].id, idUtilisateurConnecte);
            notesIndividuellesParMembre.push(studentNotes);
        }

        console.log('notesIndividuellesParMembre:', notesIndividuellesParMembre);
        console.log('notes Equipe récupérées:', notes);
        console.log('Equipe:', newEquipe);

        for (let i = 0; i < newEquipe[1].length; i++) {
            const noteIndividuelle = notesIndividuellesParMembre[i].find(note => note.typeNoteEleve === "SS_PR" && idUtilisateurConnecte == note.evaluateur.id);
            if (noteIndividuelle) {
                individuelNotes.value[i] = noteIndividuelle.note;
                individuelCommentaires.value[i] = noteIndividuelle.commentaire;
                console.log('noteIndividuelle:', individuelNotes.value);

            } else {
                individuelNotes.value[i] = 0;
                individuelCommentaires.value[i] = '';
            }
        }


        if (notes.length > 0) {
            const noteSupport = notes.find(note => note.typeNoteEquipe === "SU_PR" && idUtilisateurConnecte == note.evaluateur.id);
            const noteManagement = notes.find(note => note.typeNoteEquipe === "PR_MA" && idUtilisateurConnecte == note.evaluateur.id);
            const noteTechnique = notes.find(note => note.typeNoteEquipe === "TE_SO" && idUtilisateurConnecte == note.evaluateur.id);
            const noteConformite = notes.find(note => note.typeNoteEquipe === "SP_CO" && idUtilisateurConnecte == note.evaluateur.id);
            const noteGlobal = notes.find(note => note.typeNoteEquipe === "OT_PR" && idUtilisateurConnecte == note.evaluateur.id);
            console.log('noteSupport:', noteSupport);

            if (noteSupport) {
                supportNote.value = noteSupport.note;
                supportCommentaire.value = noteSupport.commentaire;
                console.log('supportNote:', supportNote.value);
            }

            if (noteManagement) {
                console.log('ManagementNote:', noteManagement.note);
                console.log('managementCommentaire:', noteManagement.commentaire);
                managementNote.value = noteManagement.note;
                managementCommentaire.value = noteManagement.commentaire;
                console.log('ManagementNote:', managementNote.value);
            }

            if (noteTechnique) {
                techniqueNote.value = noteTechnique.note;
                techniqueCommentaire.value = noteTechnique.commentaire;
            }
            if (noteConformite) {
                conformiteNote.value = noteConformite.note;
                conformiteCommentaire.value = noteConformite.commentaire;
            }
            if (noteGlobal) {
                globalNote.value = noteGlobal.note;
                globalCommentaire.value = noteGlobal.commentaire;
            }

        }
        if (notes.length === 0) {
            // Set default values
            managementNote.value = 0;
            supportNote.value = 0;
            managementCommentaire.value = '';
            supportCommentaire.value = '';
            techniqueNote.value = 0;
            techniqueCommentaire.value = '';
            conformiteNote.value = 0;
            conformiteCommentaire.value = '';
            globalNote.value = 0;
            globalCommentaire.value = '';

        }
    }

    dataLoaded.value = true;
});



function handleManagementCommentaireChanged(newCommentaire) {
    managementCommentaire.value = newCommentaire;
}

// Fonction pour gérer le changement de commentaire pour le support de présentation
function handleSupportCommentaireChanged(newCommentaire) {
    supportCommentaire.value = newCommentaire;
}

function handleTechniqueCommentaireChanged(newCommentaire) {
    techniqueCommentaire.value = newCommentaire;
}

function handleConformiteCommentaireChanged(newCommentaire) {
    conformiteCommentaire.value = newCommentaire;
}

function handleIndividuelCommentaireChanged(newCommentaire, index) {
    individuelCommentaires.value[index] = newCommentaire;
}

function handleGlobalCommentaireChanged(newCommentaire) {
    globalCommentaire.value = newCommentaire;
}


function ConformiteNoteChanged(newNote) {
    conformiteNote.value = newNote;
}

function TechniqueNoteChanged(newNote) {
    techniqueNote.value = newNote;
}

function ManagementNoteChanged(newNote) {
    managementNote.value = newNote;
}

// Fonction pour gérer le changement de note pour le support de présentation
function SupportNoteChanged(newNote) {
    supportNote.value = newNote;
}

function GlobalNoteChanged(newNote) {
    globalNote.value = newNote;
}

function IndividuelleNoteChanged(newNote, index) {
    individuelNotes.value[index] = newNote;
}


const handleEquipeChange = (newEquipe) => {
    equipe.value = newEquipe;
};


async function saveNote() {

    const noteEquipe = [
        {
            equipe: { id: equipe.value[0] },
            evaluateur: { id: idUtilisateurConnecte.value },
            sprint: { id: sprint.value[0].id },
            typeNoteEquipe: "PR_MA",
            note: managementNote.value,
            commentaire: managementCommentaire.value
        },
        {
            equipe: { id: equipe.value[0] },
            evaluateur: { id: idUtilisateurConnecte.value },
            sprint: { id: sprint.value[0].id },
            typeNoteEquipe: "SU_PR",
            note: supportNote.value,
            commentaire: supportCommentaire.value
        },
        {
            equipe: { id: equipe.value[0] },
            evaluateur: { id: idUtilisateurConnecte.value },
            sprint: { id: sprint.value[0].id },
            typeNoteEquipe: "TE_SO",
            note: techniqueNote.value,
            commentaire: techniqueCommentaire.value
        },
        {
            equipe: { id: equipe.value[0] },
            evaluateur: { id: idUtilisateurConnecte.value },
            sprint: { id: sprint.value[0].id },
            typeNoteEquipe: "SP_CO",
            note: conformiteNote.value,
            commentaire: conformiteCommentaire.value
        },
        {
            equipe: { id: equipe.value[0] },
            evaluateur: { id: idUtilisateurConnecte.value },
            sprint: { id: sprint.value[0].id },
            typeNoteEquipe: "OT_PR",
            note: globalNote.value,
            commentaire: globalCommentaire.value
        }

    ];

    console.log('noteEquipe:', noteEquipe);

    try {
        const response = await saveNoteToDatabase(noteEquipe);

        if (response.status === 201) {
            // Afficher un toast de succès si l'envoi est réussi
            toast.success('Les notes ont été soumises avec succès !', { position: "top-center" });
            formSubmitSuccess.value = true; // Marquer la soumission du formulaire comme réussie
            formSubmitted.value = true; // Marquer le formulaire comme soumis

        } else {
            // Afficher un toast d'erreur si la réponse n'est pas réussie
            toast.error('Une erreur s\'est produite lors de l\'envoir des notes, il n\'y a pas de réponse du serveur', { position: "top-center" });
        }
    } catch (error) {
        console.error('Failed to save note:', error);
        toast.error('Une erreur s\'est produite lors de l\'envoi des notes.', { position: "top-center" });
    }
}


async function saveIndividualNotes() {
    const notesIndividuelles = [];
    for (let i = 0; i < equipe.value[1].length; i++) {

        notesIndividuelles.push({
            eleve: { id: equipe.value[1][i].id },
            evaluateur: { id: idUtilisateurConnecte.value },
            sprint: { id: sprint.value[0].id },
            typeNoteEleve: "SS_PR",
            note: individuelNotes.value[i],
            commentaire: individuelCommentaires.value[i]
        });
    }

    console.log('notesIndividuelles:', notesIndividuelles);

    try {
        const response = await saveNoteIndividuelleToDatabase(notesIndividuelles);

        if (response.status === 201) {
            // Afficher un toast de succès si l'envoi est réussi
            toast.success('Les notes individuelles ont été soumises avec succès !', { position: "top-center" });
            formSubmitSuccess.value = true; // Marquer la soumission du formulaire comme réussie
            formSubmitted.value = true; // Marquer le formulaire comme soumis

        } else {
            // Afficher un toast d'erreur si la réponse n'est pas réussie
            toast.error('Une erreur s\'est produite lors de l\'envoir des notes individuelles, il n\'y a pas de réponse du serveur', { position: "top-center" });
        }
    } catch (error) {
        console.error('Failed to save note:', error);
        toast.error('Une erreur s\'est produite lors de l\'envoi des notes individuelles.', { position: "top-center" });
    }

}

async function getEquipesData() {
    equipes.value = await fetchEquipes();
}

async function getSprintsDate() {
    sprints.value = await getAllSprints();
}

getEquipesData();
getSprintsDate();

// Pour les échelles
const echellesNote = ref([]);
const selectedTypeEchelle = ref('');

// Fonction pour obtenir les échelles depuis le service
const getEchellesNote = async () => {
    echellesNote.value = await fetchEchellesNote();
};

// Propriété calculée pour obtenir les types uniques d'échelles
const uniqueTypes = computed(() => {
    const types = new Set();
    echellesNote.value.forEach(echelle => types.add(echelle.typeEchelle));
    return Array.from(types);
});

// Propriété calculée pour filtrer les échelles en fonction du type sélectionné
const filteredEchelles = computed(() => {
    if (!selectedTypeEchelle.value) {
        return echellesNote.value;
    } else {
        return echellesNote.value.filter(echelle => echelle.typeEchelle === selectedTypeEchelle.value);
    }
});

// Charger les échelles lorsque le composant est monté
onMounted(async () => {
    await getEchellesNote();
});


</script>


<template>
    <Suspense>
    <div class="flex flex-col h-screen w-full p-8 overflow-y-auto bg-custom-sideBarBG gap-4">

        <!-- Titre de la page -->
        <Title>Notation des équipes</Title>

        <!-- Choosers -->
        <div class="flex flex-row justify-between">
            <Chooser v-if="sprints" :sprints="sprints" type="SPRINTS" v-model="sprint" class="items-center" />
            <Chooser v-if="equipes" v-model="equipe" :equipes="equipes" :sprints="sprints" type="EQUIPES"
                @change="handleEquipeChange" class="items-center" />
        </div>

        <!-- Accordéeon global -->
        <Accordion class="bg-custom-sideBarBG gap-2 border-gray-200 border shadow-sm rounded-md" :activeIndex="0">
            <AccordionTab :header="'Notation commune'">
                <p class="text-xs font-Roboto text-custom-grey"> Notez le contenu global de la présentation
                    ainsi que le
                    support utilisé pendant l’oral. Ces notes sont communes aux étudiants de l’équipe.</p>
                <div class="flex space-x-4">
                    <BoxNotation v-if="isSupervisingStaff || idTechnicalCoaches" title="Support de présentation"
                        class="mt-4 w-64 h-32" :note="supportNote" @noteChanged="SupportNoteChanged"
                        :commentaire="supportCommentaire" @commentaireChanged="handleSupportCommentaireChanged" />
                    <BoxNotation
                        v-if="(isSupervisingStaff && equipe[0] === supervisingStaffTeamId) || idTechnicalCoaches"
                        title="Management du projet" class="mt-4 w-64 h-32" :note="managementNote"
                        @noteChanged="ManagementNoteChanged" :commentaire="managementCommentaire"
                        @commentaireChanged="handleManagementCommentaireChanged" />
                    <BoxNotation
                        v-if="(isSupervisingStaff && equipe[0] === supervisingStaffTeamId) || idTechnicalCoaches"
                        title="Solution Technique" class="mt-4 w-64 h-32" :note="techniqueNote"
                        @noteChanged="TechniqueNoteChanged" :commentaire="techniqueCommentaire"
                        @commentaireChanged="handleTechniqueCommentaireChanged" />
                    <BoxNotation
                        v-if="(isSupervisingStaff && equipe[0] === supervisingStaffTeamId) || idTechnicalCoaches"
                        title="Conformité du sprint" class="mt-4 w-64 h-32" :note="conformiteNote"
                        @noteChanged="ConformiteNoteChanged" :commentaire="conformiteCommentaire"
                        @commentaireChanged="handleConformiteCommentaireChanged" />
                    <BoxNotation v-if="isTeamMember" title="Performance Global de l'équipe" class="mt-4 w-64 h-32"
                        :note="globalNote" @noteChanged="GlobalNoteChanged" :commentaire="globalCommentaire"
                        @commentaireChanged="handleGlobalCommentaireChanged" />
                </div>
                <div class="mt-4">
                    <Button @click="saveNote" classes="px-2 py-1">Enregistrer</Button>
                </div>
            </AccordionTab>
        </Accordion>

        <!-- Accordéon encadrant -->
        <Accordion v-if="isSupervisingStaff" class="bg-custom-sideBarBG gap-2 border-gray-200 border shadow-sm rounded-md" :activeIndex="0">
            <AccordionTab :header="'Notation Individuelle'">
                <Carousel :value="equipe[1]" :numVisible="4" :numScroll="1" :showIndicators="false" class="w-full">
                    <template #item="slotProps">
                        <div :key="slotProps.data.id">
                            <BoxNotation :title="`${slotProps.data.nom} ${slotProps.data.prenom}`"
                                :note="individuelNotes[slotProps.index]"
                                @noteChanged="(newNote) => IndividuelleNoteChanged(newNote, slotProps.index)"
                                :commentaire="individuelCommentaires[slotProps.index]"
                                @commentaireChanged="(newCommentaire) => handleIndividuelCommentaireChanged(newCommentaire, slotProps.index)" />
                        </div>
                    </template>
                </Carousel>
                <div class="mt-4">
                    <Button @click="saveIndividualNotes" classes="px-2 py-1">Enregistrer</Button>
                </div>
            </AccordionTab>

        </Accordion>
        <div v-if="isSupervisingStaff">
            <!-- Sélection du type d'échelle -->
            <div class="mt-4">
                <label for="typeEchelle" class="text-lg font-CASlalomBold mt-9">Choisissez le type d'échelle :</label>
                <select v-model="selectedTypeEchelle" id="typeEchelle" name="typeEchelle"
                    class="block w-auto mt-1 py-1 px-2 rounded-md text-sm border border-gray-300">
                    <option value="">Tous les types</option>
                    <option v-for="type in uniqueTypes" :key="type" :value="type">{{ type }}</option>
                </select>
            </div>

            <!-- Affichage des échelles filtrées -->
            <div v-for="echelle in filteredEchelles" :key="echelle.id" class="mt-6">
                <Echelle :echelons="echelle.echelons" :typeEchelle="echelle.typeEchelle" />
            </div>
        </div>
    </div>
    </Suspense>
</template>
