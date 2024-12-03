<script setup>
import Dialog from 'primevue/dialog';
import Button from '../Buttons/Button.vue';
import { ref, watch } from 'vue';
import { defineProps } from 'vue';
import { createBM, modifyBM } from '../../services/BonusMalusSSService';
import { toastInjectionKey } from 'vue-toastification';
import { useToast } from 'vue-toastification';
import { deleteBM } from '../../services/BonusMalusSSService';


const props = defineProps({
    visibilite: Boolean,
    student: Object,
    value: Object,
    sprint: Object
});

const visible = ref(props.visibilite);
const valeur = ref(props.value != null ? props.value.note : 0);
const data = ref(props.value);
const commentaire = ref(data.value != null ? data.value.commentaire : '');
const sprint = ref(props.sprint[0]);
const emits = defineEmits(['update:visibilite'], ['newBM']);

const toast = useToast();

watch(() => props.visibilite, (newValue) => {
    visible.value = !visible.value;
    valeur.value = props.value != null ? props.value.note : 0;
    commentaire.value = props.value != null ? props.value.commentaire : '';
    data.value = props.value;
});

watch(() => props.value, (newValue) => {
    valeur.value = newValue != null ? newValue.note : 0;
    data.value = newValue;
    commentaire.value = newValue != null ? newValue.commentaire : '';
});

watch(() => props.sprint, (newValue) => {
    sprint.value = newValue[0];
});

async function supprimerBM(){
    console.log("supression");
    try{
        const response = await deleteBM(data.value.id);
        toast.success('Le B/M a été supprimé avec succès');
        emits('newBM');
        visible.value = false;
    }catch(e){
        console.log(e);
        toast.error('Une erreur est survenue dans la suppression du B/M');
    }
}

async function creerBM() {
    if (commentaire.value == '') {
        toast.error("Merci de saisir un commentaire en guise de justification");
        return;
    }
    if (valeur.value == 0) {
        toast.error("Merci de saisir une valeur de Bonus/Malus différente de 0");
        return;
    }
    if(valeur.value < -20 || valeur.value > 20){
        toast.error("La valeur du Bonus/Malus doit être comprise entre -20 et 20");
        return;
    }
    if (data.value != null && data.value.note != 0) {
        try{
            const response = await modifyBM(data.value.id, valeur.value, commentaire.value);
            toast.success('Le B/M a été modifié avec succès');
            emits('newBM');
            visible.value = false;
        }catch(e){
            toast.error('Une erreur est survenue dans la modification du B/M');
        }
    }
    else {
        try {
            const reponse = await createBM(props.student.id, localStorage.getItem('user_id'), sprint.value.id, valeur.value, commentaire.value);
            toast.success('Le B/M a été créé avec succès');
            emits('newBM');
            visible.value = false;
        } catch (e) {
            toast.error('Une erreur est survenue dans la création du B/M');
        }
    }
}

</script>

<template>
    <div class="card flex justify-content-center">
        <Dialog v-model:visible="visible" modal :header="'BONUS & MALUS - '" class="min-w-[500px]" id="dialogBm">
            <template #header>
                <div class="font-CASlalomBold text-xl">BONUS & MALUS <br><span class="text-sm font-CASlalomRegular">
                        {{ student.prenom }} {{ student.nom }} - {{ student.equipe.nom }} - {{ sprint.name }}</span>
                </div>
            </template>
            <div class="flex flex-col justify-start items-center p-2 gap-5">
                <input type="number" min="-20" max="20" class="text-center w-32 border-2 border-gray-200 rounded-md"
                    v-model="valeur">
                <textarea class="w-full bg-gray-100 p-3 rounded-md italic max-h-40 min-h-20 text-sm" name="" id=""
                    :placeholder="'Entrer un commentaire pour justifier le Bonus/Malus attribué à ' + student.prenom"
                    v-model="commentaire"></textarea>
            </div>
            <div class="flex justify-end items-center mt-3 gap-3">
                <Button id="supBmSS" v-if="props.value != null && props.value != 0" classes="px-4 py-2 ml-2" @click="supprimerBM()">Supprimer le BM</Button>
                <Button classes="px-4 py-2" :disabled="value === 0" @click="creerBM()">{{ (data != null && data.note !=
                    0) ?
                    'Modifier' : 'Ajouter' }}</Button>
            </div>
        </Dialog>
    </div>
</template>

<style scoped>
:deep(.p-dialog-header) {
    background: white;
    padding: 1px;
}
</style>