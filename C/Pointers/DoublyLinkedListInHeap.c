#include <stdio.h>
#include <stdlib.h>
#include <string.h>

// Defining structure for nodes

typedef struct Node{
    char *data;
    struct Node *prev;
    struct Node *next;
} Node;

//Structure for doubly linked lsit

typedef struct DoublyLinkedList {
    Node *head; // first node
    Node *tail; // last node
} DoublyLinkedList;

// Function to create a new doubly linked list
DoublyLinkedList* create_list() {
    DoublyLinkedList *list = malloc(sizeof(DoublyLinkedList));
    list->head = NULL;
    list->tail = NULL;
    return list;
}

// Node with heap-allocated string

Node* create_node(const char *str) {
    // allocate size of node in heap
    Node *new_node = malloc(sizeof(Node));
    new_node->data = malloc(strlen(str) +1); // allocate heap for string
    strcpy(new_node->data, str);
    new_node->prev = NULL;
    new_node->next = NULL;
    return new_node;
}

//Function add node with string to end of list 
void append(DoublyLinkedList *list, const char *str){
    Node *new_node = create_node(str);
    if(list->tail == NULL){
        list->head = new_node;
        list->tail = new_node;
    } else {
        list->tail->next = new_node;
        new_node->prev = list->tail;
        list->tail = new_node;
    }
}

// Function remove node from list
void remove_node(DoublyLinkedList *list, Node *node){
    if (node == NULL) return;

    if (node->prev != NULL) {
        node->prev->next = node->next;
    } else {
        list->head = node->next;
    }

    if(node->next != NULL) {
        node->next->prev = node->prev;
    } else {
        list->tail = node->prev;
    }

    free(node->data);
    free(node);
}

// function to free list 
void free_list(DoublyLinkedList *list){
    Node *current = list->head;
    while(current != NULL){
        Node *next = current ->next;
        free(current->data);
        free(current);
        current = next;
    }
    free(list);
}

// function to print list 
void print_list(DoublyLinkedList *list){
    Node *current = list->head;
    while(current != NULL){
        printf("%s -> ", current->data);
        current = current->next;
    }
    printf("NULL\n");
}


int main(){
    DoublyLinkedList *list = create_list();
    append(list, "Hello");
    append(list, "World");
    append(list, "Doubly");
    append(list, "Linked");
    append(list, "List");

    printf("Initial list:\n");
    print_list(list);

    // Remove Node
    remove_node(list, list->head->next);
    printf("After removing the second node:\n");
    print_list(list);

    // Free the entire list
    free_list(list);

    return 0;

}