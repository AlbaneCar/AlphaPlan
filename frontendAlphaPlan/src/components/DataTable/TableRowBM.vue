<template>
    <tr v-for="(member, index) in teamMembers" :key="index" class="bg-white border-b hover:bg-gray-50">
        <td class="px-6 py-2 items-center">
            <div class="flex items-center">

                <!-- Affichage du nom et prénom de chaque membre -->
                <span>{{ member.nom.toUpperCase() }} {{ member.prenom }}</span>
            </div>
        </td>
        <td class="px-6 py-2">
            <div class="main-container w-44 h-9 relative">
                <div class="absolute top-0 right-9">

                    <!-- Nombre de BM par membre -->
                    <div class="border border-gray-300 flex justify-center items-center">
                        <span :class="{
                            'text-black': member.bonusMalus === 0,
                            'text-blue-500': member.bonusMalus > 0,
                            'text-red-600 px-11': member.bonusMalus < 0
                        }" class="h-[30.5px] w-[103px] font-CASlalomBold text-lg px-12 py-1 flex justify-center items-center">{{ member.bonusMalus }}</span>
                    </div>
                </div>

            <!-- Mettre des malus -->
            <div class="w-9 h-8 bg-gray-300 rounded-lg rounded-tr-none rounded-br-none absolute top-0 left-0 z-[1]" @click="decrement(member)" style="cursor: pointer;">
                <span class="flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 left-3 text-left">-</span>
            </div>

            <!-- Mettre des bonus -->
            <div class="w-9 h-8 bg-gray-300 rounded-lg rounded-tr-none rounded-br-none absolute top-0 right-0 rotate-180 z-[2]" @click="increment(member)" style="cursor: pointer;">
                <span class="flex h-6 justify-start items-start font-CASlalomBold text-lg leading-6 text-black absolute top-1 right-3 text-left">+</span>
            </div>
            </div>
        </td>
        <td class="px-6 py-2 items-center justify-end">

            <!-- Etat de validation du BM par membre -->
            <div class="flex items-center justify-end">
                <div v-if="props.advancment == 3 || props.advancment == 4 || props.advancment == 5">
                    <div v-if="member.nbBMValides == teamMembers.length" class="flex justify-center items-center">
                        <Validate color="#62E291" :width="15" class="ml-2" />
                        <p class="ml-2 text-green-400 text-xl"><em>Validé</em></p>
                    </div>
                    <div v-else class="flex justify-center items-center">
                        <Cross color="#FB923D" :width="15" class="ml-2" />
                        <p class="ml-2 text-orange-300 text-xl"><em>Pas encore validé</em></p>
                    </div>
                </div>
                <div v-if="props.advancment == 2">
                    <em>Non disponible</em>
                </div>
            </div>
        </td>
    </tr>
</template>

<script setup>
import Validate from '../../assets/icons/Validate.vue'
import Cross from '../../assets/icons/Cross.vue'
import { useRouter } from 'vue-router';
import { ref, onMounted } from 'vue';
import { useToast } from 'vue-toastification';
import { defineProps } from 'vue'

const props = defineProps({
    advancment: Number,
    teamMembers: Array
})

const emit = defineEmits(['update-totals', 'update-TMBM']);

const router = useRouter();
const toast = useToast();

// Donner des bonus (bloqué si les valeurs sont déjà choisies)
const increment = (member) => {
    if (props.advancment != 1) {
        if (props.advancment != 3) {
            if (props.advancment != 4) {
                if (props.advancment != 5) {
                    if (member.bonusMalus < 4) {
                        member.bonusMalus++;
                        if (member.bonusMalus > 0) {
                            emit('update-totals', 'bonus', 1);
                            emit('update-TMBM', member.id, member.bonusMalus);
                        } else {
                            emit('update-totals', 'malus', -1);
                            emit('update-TMBM', member.id, member.bonusMalus);
                        }
                    } else {
                        toast.error("Le bonus ne peut pas excéder 4 points !", {
                            position: "top-center",
                        });
                    }
                } else {
                    toast.error("Les bonus/malus ont déjà été validés.", {
                    position: "top-center",
                });
                }
            } else {
                toast.error("Les bonus/malus ont déjà été validés.", {
                position: "top-center",
            });
        }
        } else {
            toast.error("Les bonus/malus ne sont pas modifiables.", {
                position: "top-center",
            });
        }
    } else {
        toast.error("Les bonus/malus ne sont pas encore disponibles.", {
            position: "top-center",
        });
    }
};

// Donner des malus (bloqué si les valeurs sont déjà choisies)
const decrement = (member) => {
    if (props.advancment != 1) {
        if (props.advancment != 3) {
            if (props.advancment != 4) {
                if (props.advancment != 5) {
                    if (member.bonusMalus > -4) {
                        member.bonusMalus--;
                        if (member.bonusMalus < 0) {
                            emit('update-totals', 'malus', 1);
                            emit('update-TMBM', member.id, member.bonusMalus);
                        } else {
                            emit('update-totals', 'bonus', -1);
                            emit('update-TMBM', member.id, member.bonusMalus);
                        }
                    } else {
                        toast.error("Le malus ne peut pas excéder 4 points !", {
                            position: "top-center",
                        });
                    }
                } else {
                toast.error("Les bonus/malus ont déjà été validés.", {
                    position: "top-center",
                });
            }
            } else {
            toast.error("Les bonus/malus ont déjà été validés.", {
                position: "top-center",
            });
        }
        } else {
            toast.error("Les bonus/malus ne sont pas modifiables.", {
                position: "top-center",
            });
        }
    } else {
        toast.error("Les bonus/malus ne sont pas encore disponibles.", {
            position: "top-center",
        });
    }
};

</script>