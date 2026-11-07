package com.thiqa.backend.thiqa.backend.entity;

public enum OrderStatus {
  PENDING, // placed, waiting for the store
  CONFIRMED, // store accepted it
  REJECTED, // store rejected it (manual or auto-reject)
  CANCELLED, // customer cancelled before delivery
  OUT_FOR_DELIVERY,
  DELIVERED, // customer received and paid
  REFUSED // customer refused at the door
}
